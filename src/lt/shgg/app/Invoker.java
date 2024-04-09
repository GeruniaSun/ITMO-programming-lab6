package lt.shgg.app;

import lt.shgg.commands.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <h1>Класс инициатора</h1>
 * инициатор - сущность запускающая команды и хранящая их список
 */
public class Invoker {
    private final Map<String, Command> commandMap = new HashMap<>();

    /**
     * Конструктор, задающий все доступные пользователю команды
     * @param receiver объект приемника экземпляр класса {@link Receiver}
     */
    public Invoker(Receiver receiver){
        commandMap.put("info", new Info(receiver));
        commandMap.put("help", new Help(receiver, commandMap.values()));
        commandMap.put("show", new Show(receiver));
        commandMap.put("exit", new Exit(receiver));
        commandMap.put("add", new Add(receiver));
        commandMap.put("print_descending", new PrintDescending(receiver));
        commandMap.put("filter_by_type", new FilterByType(receiver));
        commandMap.put("count_greater_than_type", new CountGreaterThanType(receiver));
        commandMap.put("clear", new Clear(receiver));
        commandMap.put("remove_by_id", new RemoveById(receiver));
        commandMap.put("save", new Save(receiver));
        commandMap.put("remove_greater", new RemoveGreater(receiver));
        commandMap.put("remove_lower", new RemoveLower(receiver));
        commandMap.put("add_if_max", new AddIfMax(receiver));
        commandMap.put("update", new Update(receiver));
        commandMap.put("execute_script", new ExecuteScript(receiver));
    }

    /**
     * Метод для выполнения команды
     * @param commandName имя команды
     * @param args аргументы введенные пользователем вместе с командой
     */
    void runCommand(String commandName, List<String> args){
        Command command = this.commandMap.get(commandName);
        if (command == null) throw new NullPointerException("Такой команды не существует. " + "\n" +
                "Используйте 'help' чтоб получить справку по доступным командам");
        command.execute(args);
    }
}
