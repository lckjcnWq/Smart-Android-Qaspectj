package com.example.qaspectj.permission.aspectj;

import android.app.Fragment;
import android.content.Context;
import android.util.Log;
import com.example.qaspectj.permission.annotion.Permission;
import com.example.qaspectj.permission.annotion.PermissionCancle;
import com.example.qaspectj.permission.annotion.PermissionDenied;
import com.example.qaspectj.permission.interfaces.PermissionRequstCallback;
import com.example.qaspectj.permission.utils.PermissionUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;


@Aspect
public class PermissionAspectJ {

    private final static String TAG=PermissionAspectJ.class.getName();

    /**
     * 声明切入点
     * @param permission  注解中的参数
     */
    @Pointcut("execution(@Permission * *(..))&& @annotation(permission)")
    public void getPermission(Permission permission){}

    /**
     * 获取切入的方法
     * @param point
     * @param permission
     * @throws Throwable
     */
    @Around("getPermission(permission)")
    public void getPointMethod(final ProceedingJoinPoint point, Permission permission) throws Throwable {
        Context context = null;
        //获取上下文对象
        final Object thisContext=point.getThis();

        if(thisContext instanceof Context){
            context= (Context) thisContext;
        }else if(thisContext instanceof Fragment){
            context=((Fragment) thisContext).getActivity();
        }

        if(context==null ||permission==null ||permission.value().length<=0){
            return;
        }
        //获取权限数据
        String[] permissinValue=permission.value();
        final int requstCode=permission.requestCode();
        PermissionUtil.launchActivity(context, permissinValue, requstCode, new PermissionRequstCallback() {
            @Override
            public void permissionSuccess() {
                //权限申请成功 执行切入的方法
                try {
                    point.proceed();
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }

            @Override
            public void permissionCancel() {
               PermissionUtil.invokeAnnotation(thisContext, PermissionCancle.class,requstCode);
            }

            @Override
            public void permissionDenied() {
                PermissionUtil.invokeAnnotation(thisContext, PermissionDenied.class,requstCode);
            }
        });
        Log.d(TAG,"getPointMethod --00--="+  point.getThis().getClass().getCanonicalName());
        Log.d(TAG,"getPointMethod --11--="+  point.getThis().getClass().getName());
    }
}
