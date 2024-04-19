import lt.shgg.app.Receiver;
import lt.shgg.network.Request;

import java.io.*;
import java.net.*;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.logging.Logger;

public class Server {

    ServerSocketChannel ss;

    private final int port;

    private static final Logger serverLogger = Logger.getLogger("logger");

    BufferedInputStream bf = new BufferedInputStream(System.in);
    BufferedReader scanner = new BufferedReader(new InputStreamReader(bf));
    private final Receiver receiver;

    public Server(int port, Receiver receiver) {
        this.port = port;
        this.receiver = receiver;
    }

    public void run() {
        try {
            openServerSocket();
            serverLogger.info("Добро пожаловать на сервер шизофрения");

            while (true) {
                if (scanner.ready()) {
                    String line = scanner.readLine();
                    if (line.equals("save")) {
                        this.receiver.save();
                        serverLogger.info("Объекты успешно сохранены");
                    } else if (line.equals("exit")) {
                        this.receiver.save();
                        serverLogger.info("Объекты успешно сохранены");
                        System.exit(0);
                    } else System.out.println("не реагирую...");
                }

                SocketChannel clientSocket = ss.accept();
                if (clientSocket != null) {
                    processClientRequest(clientSocket);
                }
            }
        } catch (IOException e) {
            serverLogger.warning("Произошла ошибка при работе сервера" + e.getMessage());
        }
    }

    private void openServerSocket() {
        try {
            ss = ServerSocketChannel.open();
            ss.bind(new InetSocketAddress(port));
            ss.configureBlocking(false);
        } catch (IOException exception) {
            serverLogger.warning("Произошла ошибка при попытке использовать порт");
        }
    }

    private void processClientRequest(SocketChannel clientSocket) {
        Request userRequest;
        try (ObjectInputStream clientReader = new ObjectInputStream(clientSocket.socket().getInputStream());
             ObjectOutputStream clientWriter = new ObjectOutputStream(clientSocket.socket().getOutputStream())) {

            userRequest = (Request) clientReader.readObject();
            serverLogger.info("Получен запрос " + userRequest);
            var invoker = new Invoker();
            var responseToUser = invoker.runCommand(userRequest, this.receiver);
            clientWriter.writeObject(responseToUser);
            serverLogger.info("Отправлен ответ " + responseToUser.getResult());
            clientWriter.flush();
        } catch (ClassNotFoundException | InvalidClassException | NotSerializableException e) {
            serverLogger.warning("Произошла ошибка при взаимодействии с клиентом!");
        } catch (IOException exception) {
            serverLogger.warning("Ошибка ввода вывода " + exception.getMessage());
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                serverLogger.warning("Ошибка при закрытии клиентского сокета");
            }
        }
    }
}
