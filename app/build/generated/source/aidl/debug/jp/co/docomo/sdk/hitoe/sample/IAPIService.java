/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: C:\\Users\\Tomoki\\Documents\\AndroidStudioProjects\\Sample\\app\\src\\main\\aidl\\jp\\co\\docomo\\sdk\\hitoe\\sample\\IAPIService.aidl
 */
package jp.co.docomo.sdk.hitoe.sample;
// Declare any non-default types here with import statements

public interface IAPIService extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements jp.co.docomo.sdk.hitoe.sample.IAPIService
{
private static final java.lang.String DESCRIPTOR = "jp.co.docomo.sdk.hitoe.sample.IAPIService";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an jp.co.docomo.sdk.hitoe.sample.IAPIService interface,
 * generating a proxy if needed.
 */
public static jp.co.docomo.sdk.hitoe.sample.IAPIService asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof jp.co.docomo.sdk.hitoe.sample.IAPIService))) {
return ((jp.co.docomo.sdk.hitoe.sample.IAPIService)iin);
}
return new jp.co.docomo.sdk.hitoe.sample.IAPIService.Stub.Proxy(obj);
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
case TRANSACTION_registCallback:
{
data.enforceInterface(DESCRIPTOR);
jp.co.docomo.sdk.hitoe.sample.IAPIServiceCallback _arg0;
_arg0 = jp.co.docomo.sdk.hitoe.sample.IAPIServiceCallback.Stub.asInterface(data.readStrongBinder());
this.registCallback(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_unregistCallback:
{
data.enforceInterface(DESCRIPTOR);
this.unregistCallback();
reply.writeNoException();
return true;
}
case TRANSACTION_registExTypes:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
this.registExTypes(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_unregistExTypes:
{
data.enforceInterface(DESCRIPTOR);
this.unregistExTypes();
reply.writeNoException();
return true;
}
case TRANSACTION_getMeasureFlag:
{
data.enforceInterface(DESCRIPTOR);
boolean _result = this.getMeasureFlag();
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_getAvailableSensor:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
java.lang.String _arg1;
_arg1 = data.readString();
int _result = this.getAvailableSensor(_arg0, _arg1);
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_connect:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
java.lang.String _arg1;
_arg1 = data.readString();
java.lang.String _arg2;
_arg2 = data.readString();
java.lang.String _arg3;
_arg3 = data.readString();
int _result = this.connect(_arg0, _arg1, _arg2, _arg3);
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_disconnect:
{
data.enforceInterface(DESCRIPTOR);
int _result = this.disconnect();
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_getAvailableData:
{
data.enforceInterface(DESCRIPTOR);
int _result = this.getAvailableData();
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_getAvailableEx:
{
data.enforceInterface(DESCRIPTOR);
int _result = this.getAvailableEx();
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_addRawReceiver:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
java.lang.String _arg1;
_arg1 = data.readString();
int _result = this.addRawReceiver(_arg0, _arg1);
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_addBaReceiver:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
java.lang.String _arg1;
_arg1 = data.readString();
int _result = this.addBaReceiver(_arg0, _arg1);
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_addExReceiver:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
java.lang.String _arg1;
_arg1 = data.readString();
java.lang.String _arg2;
_arg2 = data.readString();
int _result = this.addExReceiver(_arg0, _arg1, _arg2);
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_removeRawReceiver:
{
data.enforceInterface(DESCRIPTOR);
int _result = this.removeRawReceiver();
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_removeBaReceiver:
{
data.enforceInterface(DESCRIPTOR);
int _result = this.removeBaReceiver();
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_getStatus:
{
data.enforceInterface(DESCRIPTOR);
int _result = this.getStatus();
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_finish:
{
data.enforceInterface(DESCRIPTOR);
this.finish();
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements jp.co.docomo.sdk.hitoe.sample.IAPIService
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
@Override public void registCallback(jp.co.docomo.sdk.hitoe.sample.IAPIServiceCallback callback) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeStrongBinder((((callback!=null))?(callback.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_registCallback, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void unregistCallback() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_unregistCallback, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void registExTypes(java.lang.String types) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(types);
mRemote.transact(Stub.TRANSACTION_registExTypes, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void unregistExTypes() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_unregistExTypes, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public boolean getMeasureFlag() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getMeasureFlag, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public int getAvailableSensor(java.lang.String sensorType, java.lang.String param) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(sensorType);
_data.writeString(param);
mRemote.transact(Stub.TRANSACTION_getAvailableSensor, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public int connect(java.lang.String sensorType, java.lang.String sensorId, java.lang.String connectionMode, java.lang.String param) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(sensorType);
_data.writeString(sensorId);
_data.writeString(connectionMode);
_data.writeString(param);
mRemote.transact(Stub.TRANSACTION_connect, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public int disconnect() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_disconnect, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public int getAvailableData() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getAvailableData, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public int getAvailableEx() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getAvailableEx, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public int addRawReceiver(java.lang.String dataKey, java.lang.String param) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(dataKey);
_data.writeString(param);
mRemote.transact(Stub.TRANSACTION_addRawReceiver, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public int addBaReceiver(java.lang.String dataKey, java.lang.String param) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(dataKey);
_data.writeString(param);
mRemote.transact(Stub.TRANSACTION_addBaReceiver, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public int addExReceiver(java.lang.String dataKey, java.lang.String param, java.lang.String data) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(dataKey);
_data.writeString(param);
_data.writeString(data);
mRemote.transact(Stub.TRANSACTION_addExReceiver, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public int removeRawReceiver() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_removeRawReceiver, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public int removeBaReceiver() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_removeBaReceiver, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public int getStatus() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getStatus, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public void finish() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_finish, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_registCallback = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_unregistCallback = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_registExTypes = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
static final int TRANSACTION_unregistExTypes = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
static final int TRANSACTION_getMeasureFlag = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
static final int TRANSACTION_getAvailableSensor = (android.os.IBinder.FIRST_CALL_TRANSACTION + 5);
static final int TRANSACTION_connect = (android.os.IBinder.FIRST_CALL_TRANSACTION + 6);
static final int TRANSACTION_disconnect = (android.os.IBinder.FIRST_CALL_TRANSACTION + 7);
static final int TRANSACTION_getAvailableData = (android.os.IBinder.FIRST_CALL_TRANSACTION + 8);
static final int TRANSACTION_getAvailableEx = (android.os.IBinder.FIRST_CALL_TRANSACTION + 9);
static final int TRANSACTION_addRawReceiver = (android.os.IBinder.FIRST_CALL_TRANSACTION + 10);
static final int TRANSACTION_addBaReceiver = (android.os.IBinder.FIRST_CALL_TRANSACTION + 11);
static final int TRANSACTION_addExReceiver = (android.os.IBinder.FIRST_CALL_TRANSACTION + 12);
static final int TRANSACTION_removeRawReceiver = (android.os.IBinder.FIRST_CALL_TRANSACTION + 13);
static final int TRANSACTION_removeBaReceiver = (android.os.IBinder.FIRST_CALL_TRANSACTION + 14);
static final int TRANSACTION_getStatus = (android.os.IBinder.FIRST_CALL_TRANSACTION + 15);
static final int TRANSACTION_finish = (android.os.IBinder.FIRST_CALL_TRANSACTION + 16);
}
public void registCallback(jp.co.docomo.sdk.hitoe.sample.IAPIServiceCallback callback) throws android.os.RemoteException;
public void unregistCallback() throws android.os.RemoteException;
public void registExTypes(java.lang.String types) throws android.os.RemoteException;
public void unregistExTypes() throws android.os.RemoteException;
public boolean getMeasureFlag() throws android.os.RemoteException;
public int getAvailableSensor(java.lang.String sensorType, java.lang.String param) throws android.os.RemoteException;
public int connect(java.lang.String sensorType, java.lang.String sensorId, java.lang.String connectionMode, java.lang.String param) throws android.os.RemoteException;
public int disconnect() throws android.os.RemoteException;
public int getAvailableData() throws android.os.RemoteException;
public int getAvailableEx() throws android.os.RemoteException;
public int addRawReceiver(java.lang.String dataKey, java.lang.String param) throws android.os.RemoteException;
public int addBaReceiver(java.lang.String dataKey, java.lang.String param) throws android.os.RemoteException;
public int addExReceiver(java.lang.String dataKey, java.lang.String param, java.lang.String data) throws android.os.RemoteException;
public int removeRawReceiver() throws android.os.RemoteException;
public int removeBaReceiver() throws android.os.RemoteException;
public int getStatus() throws android.os.RemoteException;
public void finish() throws android.os.RemoteException;
}
