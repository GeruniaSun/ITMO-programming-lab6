package lt.shgg.commands;

import lt.shgg.app.Receiver;

import java.util.List;

/**
 * <h1>Команда print_descending</h1>
 * класс инкапсулирующий объект команды print_descending
 */
public class PrintDescending implements Command {
    /**
     * Поле для связи с объектом приемника класса {@link Receiver} выполняющим команду
     */
    private final Receiver receiver;

    /**
     * Стандартный конструктор
     * @param receiver объект приемника
     */
    public PrintDescending(Receiver receiver){
        this.receiver = receiver;
    }

    /**
     * Переопределенные методы из интерфейса {@link commands.Command}
     * логика описана в самом интерфейсе
     */
    @Override
    public void execute(List<String> args) {
        if (args != null) throw new IllegalArgumentException("Команда print_descending не принимает никаких аргументов");
        receiver.printDescending();
    }

    @Override
    public String description() {
        return "print_descending - выводит все элементы коллекции в порядке убывания";
    }
}
