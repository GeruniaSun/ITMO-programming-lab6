package lt.shgg.commands;

import lt.shgg.app.Receiver;

import java.util.List;

/**
 * <h1>Команда save</h1>
 * класс инкапсулирующий объект команды save
 */
public class Save implements Command{
    /**
     * Поле для связи с объектом приемника класса {@link Receiver} выполняющим команду
     */
    private final Receiver receiver;

    /**
     * Стандартный конструктор
     * @param receiver объект приемника
     */
    public Save(Receiver receiver){
        this.receiver = receiver;
    }

    /**
     * Переопределенные методы из интерфейса {@link commands.Command}
     * логика описана в самом интерфейсе
     */
    @Override
    public void execute(List<String> args) {
        if (args != null) throw new IllegalArgumentException("Команда save не принимает никаких аргументов");
        receiver.save();
    }

    @Override
    public String description() {
        return "save - сохраняет коллекцию в файл, указанный при запуске";
    }
}
