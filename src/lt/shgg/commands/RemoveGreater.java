package lt.shgg.commands;

import lt.shgg.app.Receiver;

import java.util.List;

/**
 * <h1>Команда remove_greater</h1>
 * класс инкапсулирующий объект команды remove_greater
 */
public class RemoveGreater implements Command{
    /**
     * Поле для связи с объектом приемника класса {@link Receiver} выполняющим команду
     */
    private final Receiver receiver;

    /**
     * Стандартный конструктор
     * @param receiver объект приемника
     */
    public RemoveGreater(Receiver receiver){
        this.receiver = receiver;
    }

    /**
     * Переопределенные методы из интерфейса {@link commands.Command}
     * логика описана в самом интерфейсе
     */
    @Override
    public void execute(List<String> args) {
        if (this.receiver.isConsoleFlag() && args != null)
            throw new IllegalArgumentException("Команда remove_greater не принимает никаких аргументов, " +
                "значение element нужно вводить с новой строки");
        receiver.removeGreater(args);
    }

    @Override
    public String description() {
        return "remove_greater {element} - удаляет из коллекции все элементы, меньшие, чем заданный";
    }
}
