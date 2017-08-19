package com.wyf.view;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wyf.view.annotation.BindContent;
import com.wyf.view.annotation.BindView;
import com.wyf.view.annotation.OnClick;
import com.wyf.view.annotation.OnLongClick;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * Created by wyf on 2017/8/19.
 */

public class ViewUtils {

    public static void inject(Activity activity) {
        if (activity == null) return;
        BindContent content = activity.getClass().getAnnotation(BindContent.class);
        if (content == null) return;
        int layoutId = content.value();
        if (layoutId <= 0) return;
        activity.setContentView(layoutId);
        View v = activity.getWindow().getDecorView();
        if (v == null) return;
        Field[] fields = activity.getClass().getDeclaredFields();
        injectFields(activity, v, fields);
        Method[] methods = activity.getClass().getDeclaredMethods();
        injectMethods(activity, v, methods);
    }

    public static View inject(Object obj, LayoutInflater inflater, ViewGroup container) {
        if (obj == null || inflater == null) return null;
        BindContent content = obj.getClass().getAnnotation(BindContent.class);
        if (content == null) return null;
        int layoutId = content.value();
        if (layoutId <= 0) return null;
        View v = container == null ? inflater.inflate(layoutId, null) : inflater.inflate(layoutId, container, false);
        if (v == null) return null;
        Field[] fields = obj.getClass().getDeclaredFields();
        injectFields(obj, v, fields);
        Method[] methods = obj.getClass().getDeclaredMethods();
        injectMethods(obj, v, methods);
        return v;
    }

    public static View inject(Object obj, LayoutInflater inflater) {
        return inject(obj, inflater, null);
    }

    public static void inject(Object obj, View v) {
        if (obj == null || v == null) return;
        Field[] fields = obj.getClass().getDeclaredFields();
        injectFields(obj, v, fields);
        Method[] methods = obj.getClass().getDeclaredMethods();
        injectMethods(obj, v, methods);
    }

    private static void injectMethods(Object obj, View v, Method[] methods) {
        if (obj == null || v == null || methods == null || methods.length == 0) return;
        for (Method m : methods) {
            // 不注入静态方法
            if (Modifier.isStatic(m.getModifiers())) continue;
            OnClick onClick = m.getAnnotation(OnClick.class);
            OnLongClick onLongClick = m.getAnnotation(OnLongClick.class);
            if (onClick == null && onLongClick == null || onClick != null && onLongClick != null) continue;
            int[] ids = onClick == null ? onLongClick.value() : onClick.value();
            MyListener listener = new MyListener(obj, m);
            for (int id : ids) {
                View view = v.findViewById(id);
                if (view == null) continue;
                if (onClick == null) {
                    view.setOnLongClickListener(listener);
                } else {
                    view.setOnClickListener(listener);
                }
            }
        }
    }

    private static void injectFields(Object obj, View v, Field[] fields) {
        if (obj == null || v == null || fields == null || fields.length == 0) return;
        for (Field f : fields) {
            BindView bindView = f.getAnnotation(BindView.class);
            if (bindView == null) continue;
            Class<?> fieldType = f.getType();
            if (
                /* 不注入静态字段 */     Modifier.isStatic(f.getModifiers()) ||
                /* 不注入final字段 */    Modifier.isFinal(f.getModifiers()) ||
                /* 不注入基本类型字段 */  fieldType.isPrimitive() ||
                /* 不注入数组类型字段 */  fieldType.isArray()) {
                continue;
            }
            int id = bindView.value();
            try {
                f.setAccessible(true);
                f.set(obj, v.findViewById(id));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private static class MyListener implements View.OnClickListener, View.OnLongClickListener {

        private Object obj;
        private Method m;

        public MyListener(Object obj, Method m) {
            this.obj = obj;
            this.m = m;
        }

        @Override
        public void onClick(View v) {
            try {
                m.setAccessible(true);
                m.invoke(obj, v);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        @Override
        public boolean onLongClick(View v) {
            try {
                m.setAccessible(true);
                boolean b = (Boolean) m.invoke(obj, v);
                return b;
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            return false;
        }
    }

}
