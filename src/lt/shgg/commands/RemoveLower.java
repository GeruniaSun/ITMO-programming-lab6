package lt.shgg.commands;

import lt.shgg.app.Receiver;

import java.util.List;

/**
 * <h1>Команда remove_lower</h1>
 * класс инкапсулирующий объект команды remove_lower
 */
public class RemoveLower implements Command{
    /**
     * Поле для связи с объектом приемника класса {@link Receiver} выполняющим команду
     */
    private final Receiver receiver;

    /**
     * Стандартный конструктор
     * @param receiver объект приемника
     */
    public RemoveLower(Receiver receiver){
        this.receiver = receiver;
    }

    /**
     * Переопределенные методы из интерфейса {@link commands.Command}
     * логика описана в самом интерфейсе
     */
    @Override
    public void execute(List<String> args) {
        if (this.receiver.isConsoleFlag() && args != null)
            throw new IllegalArgumentException("Команда remove_lower не принимает никаких аргументов, " +
                "значение element нужно вводить с новой строки");
        receiver.removeLower(args);
    }

    @Override
    public String description() {
        return "remove_lower {element} - удаляет из коллекции все элементы, большие, чем заданный";
    }
}
