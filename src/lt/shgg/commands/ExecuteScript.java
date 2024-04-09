package lt.shgg.commands;

import lt.shgg.app.Receiver;

import java.io.FileNotFoundException;
import java.nio.file.AccessDeniedException;
import java.util.List;

/**
 * <h1>Команда execute_script</h1>
 * класс инкапсулирующий объект команды execute_script
 */
public class ExecuteScript implements Command{
    /**
     * Поле для связи с объектом приемника класса {@link Receiver} выполняющим команду
     */
    private final Receiver receiver;

    /**
     * Стандартный конструктор
     * @param receiver объект приемника
     */
    public ExecuteScript(Receiver receiver){
        this.receiver = receiver;
    }

    /**
     * Переопределенные методы из интерфейса {@link commands.Command}
     * логика описана в самом интерфейсе
     */
    @Override
    public void execute(List<String> args) {
        if (args == null || args.isEmpty())
            throw new NullPointerException("Команда execute_script не работает без аргумента filename");
        if (args.size() > 1)
            throw new IllegalArgumentException("Команда execute_script принимает только 1 аргумент");
        var filename = args.get(0);
        try {
            receiver.executeScript(filename);
        } catch (AccessDeniedException | FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public String description() {
        return "execute_script filename - выполняет команды из файла filename";
    }
}
