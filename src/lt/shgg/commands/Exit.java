package lt.shgg.commands;

import lt.shgg.app.Receiver;

import java.util.List;

/**
 * <h1>Команда exit</h1>
 * класс инкапсулирующий объект команды exit
 */
public class Exit implements Command{
    /**
     * Поле для связи с объектом приемника класса {@link Receiver} выполняющим команду
     */
    private final Receiver receiver;

    /**
     * Стандартный конструктор
     * @param receiver объект приемника
     */
    public Exit(Receiver receiver) {
        this.receiver = receiver;
    }

    /**
     * Переопределенные методы из интерфейса {@link commands.Command}
     * логика описана в самом интерфейсе
     */
    @Override
    public void execute(List<String> args) {
        if (args != null) throw new IllegalArgumentException("Команда exit не принимает никаких аргументов");
        receiver.exit();
    }

    @Override
    public String description() {
        return "exit - завершает работу приложения";
    }
}
