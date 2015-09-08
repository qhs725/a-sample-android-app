LOCAL_PATH := $(call my-dir)/Net
include $(CLEAR_VARS)

LOCAL_MODULE := PocoNet
LOCAL_C_INCLUDES := $(LOCAL_PATH)/../include
LOCAL_EXPORT_C_INCLUDES += $(LOCAL_PATH)/../include
LOCAL_CFLAGS := -DPOCO_ANDROID -DPOCO_NO_FPENVIRONMENT -DPOCO_NO_WSTRING -DPOCO_NO_SHAREDMEMORY
LOCAL_CPPFLAGS := -frtti -fexceptions -I
LOCAL_SRC_FILES := \
SocketStream.cpp \
HTTPRequestHandlerFactory.cpp \
HTTPFixedLengthStream.cpp \
HTTPStream.cpp \
StreamSocketImpl.cpp \
TCPServer.cpp \
DNS.cpp \
ServerSocketImpl.cpp \
PartHandler.cpp \
RawSocketImpl.cpp \
ICMPPacket.cpp \
QuotedPrintableDecoder.cpp \
HTTPStreamFactory.cpp \
SocketAddress.cpp \
HTTPServerResponseImpl.cpp \
StringPartSource.cpp \
HTTPServerResponse.cpp \
TCPServerConnectionFactory.cpp \
FTPStreamFactory.cpp \
HTTPClientSession.cpp \
HTTPResponse.cpp \
HTTPBasicCredentials.cpp \
HostEntry.cpp \
HTTPMessage.cpp \
DatagramSocket.cpp \
NetException.cpp \
POP3ClientSession.cpp \
NetworkInterface.cpp \
ServerSocket.cpp \
HTTPHeaderStream.cpp \
MailMessage.cpp \
MailRecipient.cpp \
HTTPServerConnection.cpp \
HTTPServer.cpp \
SocketImpl.cpp \
HTTPRequestHandler.cpp \
MulticastSocket.cpp \
HTTPSessionFactory.cpp \
HTTPChunkedStream.cpp \
ICMPSocket.cpp \
HTTPIOStream.cpp \
FilePartSource.cpp \
HTTPServerConnectionFactory.cpp \
RawSocket.cpp \
AbstractHTTPRequestHandler.cpp \
TCPServerConnection.cpp \
SocketNotification.cpp \
HTTPRequest.cpp \
HTMLForm.cpp \
Socket.cpp \
PartSource.cpp \
HTTPCookie.cpp \
SocketReactor.cpp \
ICMPv4PacketImpl.cpp \
HTTPSession.cpp \
HTTPSessionInstantiator.cpp \
NullPartHandler.cpp \
IPAddress.cpp \
DialogSocket.cpp \
MailStream.cpp \
FTPClientSession.cpp \
HTTPServerRequestImpl.cpp \
MultipartReader.cpp \
HTTPServerRequest.cpp \
HTTPServerParams.cpp \
MessageHeader.cpp \
NameValueCollection.cpp \
HTTPBufferAllocator.cpp \
ICMPSocketImpl.cpp \
HTTPServerSession.cpp \
QuotedPrintableEncoder.cpp \
ICMPClient.cpp \
SocketNotifier.cpp \
MultipartWriter.cpp \
DatagramSocketImpl.cpp \
StreamSocket.cpp \
TCPServerDispatcher.cpp \
ICMPPacketImpl.cpp \
SMTPClientSession.cpp \
RemoteSyslogListener.cpp \
TCPServerParams.cpp \
RemoteSyslogChannel.cpp \
MediaType.cpp \
ICMPEventArgs.cpp \

include $(BUILD_STATIC_LIBRARY)
