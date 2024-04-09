package lt.shgg.commands;

import lt.shgg.app.Receiver;

import java.util.List;

/**
 * <h1>Команда add_if_max</h1>
 * класс инкапсулирующий объект команды add_if_max
 */
public class AddIfMax implements Command{
    /**
     * Поле для связи с объектом приемника класса {@link Receiver} выполняющим команду
     */
    private final Receiver receiver;

    /**
     * Стандартный конструктор
     * @param receiver объект приемника
     */
    public AddIfMax(Receiver receiver){
        this.receiver = receiver;
    }

    /**
     * Переопределенные методы из интерфейса {@link commands.Command}
     * логика описана в самом интерфейсе
     */
    @Override
    public void execute(List<String> args) {
        if (this.receiver.isConsoleFlag() && args != null) throw new IllegalArgumentException("Команда add_if_max не принимает никаких аргументов, " +
                "значение element нужно вводить с новой строки");
        receiver.addIfMax(args);
    }

    @Override
    public String description() {
        return "add_if_max {element} - добавляет элемент в коллекцию, если он превышает максимальный";
    }
}