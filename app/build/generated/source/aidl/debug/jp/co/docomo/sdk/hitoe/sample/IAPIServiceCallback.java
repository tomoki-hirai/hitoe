/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: C:\\Users\\Tomoki\\Documents\\AndroidStudioProjects\\Sample\\app\\src\\main\\aidl\\jp\\co\\docomo\\sdk\\hitoe\\sample\\IAPIServiceCallback.aidl
 */
package jp.co.docomo.sdk.hitoe.sample;
// Declare any non-default types here with import statements

public interface IAPIServiceCallback extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements jp.co.docomo.sdk.hitoe.sample.IAPIServiceCallback
{
private static final java.lang.String DESCRIPTOR = "jp.co.docomo.sdk.hitoe.sample.IAPIServiceCallback";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an jp.co.docomo.sdk.hitoe.sample.IAPIServiceCallback interface,
 * generating a proxy if needed.
 */
public static jp.co.docomo.sdk.hitoe.sample.IAPIServiceCallback asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof jp.co.docomo.sdk.hitoe.sample.IAPIServiceCallback))) {
return ((jp.co.docomo.sdk.hitoe.sample.IAPIServiceCallback)iin);
}
return new jp.co.docomo.sdk.hitoe.sample.IAPIServiceCallback.Stub.Proxy(obj);
}
@Override public android.os.IBinder asBinder()
{
return this;
}
@Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
{
switch (code)
{
case INTERFACE_TRANSACTION:
{
reply.writeString(DESCRIPTOR);
return true;
}
case TRANSACTION_onResponse:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
int _arg1;
_arg1 = data.readInt();
java.lang.String _arg2;
_arg2 = data.readString();
this.onResponse(_arg0, _arg1, _arg2);
reply.writeNoException();
return true;
}
case TRANSACTION_onDataReceive:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
int _arg1;
_arg1 = data.readInt();
java.lang.String _arg2;
_arg2 = data.readString();
java.lang.String _arg3;
_arg3 = data.readString();
this.onDataReceive(_arg0, _arg1, _arg2, _arg3);
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements jp.co.docomo.sdk.hitoe.sample.IAPIServiceCallback
{
private android.os.IBinder mRemote;
Proxy(android.os.IBinder remote)
{
mRemote = remote;
}
@Override public android.os.IBinder asBinder()
{
return mRemote;
}
public java.lang.String getInterfaceDescriptor()
{
return DESCRIPTOR;
}
@Override public void onResponse(int api_id, int response_id, java.lang.String responseString) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(api_id);
_data.writeInt(response_id);
_data.writeString(responseString);
mRemote.transact(Stub.TRANSACTION_onResponse, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void onDataReceive(java.lang.String connectionId, int response_id, java.lang.String dataKey, java.lang.String rawData) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(connectionId);
_data.writeInt(response_id);
_data.writeString(dataKey);
_data.writeString(rawData);
mRemote.transact(Stub.TRANSACTION_onDataReceive, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_onResponse = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_onDataReceive = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
}
public void onResponse(int api_id, int response_id, java.lang.String responseString) throws android.os.RemoteException;
public void onDataReceive(java.lang.String connectionId, int response_id, java.lang.String dataKey, java.lang.String rawData) throws android.os.RemoteException;
}
