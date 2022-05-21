// IBinderPool.aidl
package com.example.binderpooldemo;

// Declare any non-default types here with import statements

interface IBinderPool {
    IBinder queryBinder(in int binderCode);
}