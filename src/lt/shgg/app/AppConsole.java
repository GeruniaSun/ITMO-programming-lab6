package lt.shgg.app;

import java.io.InputStreamReader;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * <h1>Класс консоли</h1>
 * объект класса представляет консоль приложения считывающую команды
 */
public class AppConsole {
    /**
     * Метод запускающий консоль
     * @param inStream поток ввода (стандартный или файловый)
     * @param invoker ссылка на объект инициатора {@link app.Invoker}
     */
    public void runApp(InputStreamReader inStream, Invoker invoker){
        Scanner in = new Scanner(inStream);
        while (in.hasNextLine()){
            try {
                var input = in.nextLine();
                if (input.isBlank()) {throw new NullPointerException("Вы ничего не ввели");}
                List<String> commandWithArgs = List.of(input.split(" "));
                String commandName = commandWithArgs.get(0);
                List<String> commandArguments = null;
                if (commandWithArgs.size() >= 2){
                    commandArguments = commandWithArgs.subList(1, commandWithArgs.size());
                }
                invoker.runCommand(commandName, commandArguments);
            } catch (NoSuchElementException e) {
                System.out.println("вы какие-то гадости делаете. Закрываю приложение");
                System.exit(999);
            }
            catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
    }
}
