package lt.shgg.commands;

import lt.shgg.app.Receiver;

import java.util.List;

/**
 * <h1>Команда add</h1>
 * класс инкапсулирующий объект команды add
 */
public class Add implements Command{
    /**
     * Поле для связи с объектом приемника класса {@link Receiver} выполняющим команду
     */
    private final Receiver receiver;

    /**
     * Стандартный конструктор
     * @param receiver объект приемника
     */
    public Add(Receiver receiver) {
        this.receiver = receiver;
    }

    /**
     * Переопределенные методы из интерфейса {@link commands.Command}
     * логика описана в самом интерфейсе
     */
    @Override
    public void execute(List<String> args) {
        if (args != null && this.receiver.isConsoleFlag())
            throw new IllegalArgumentException("Команда add не принимает никаких аргументов");
        receiver.add(args);
    }

    @Override
    public String description() {
        return "add - добавляет элемент в коллекцию";
    }
}
