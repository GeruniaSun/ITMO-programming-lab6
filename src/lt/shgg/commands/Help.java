package lt.shgg.commands;

import lt.shgg.app.Receiver;

import java.util.Collection;
import java.util.List;

/**
 * <h1>Команда help</h1>
 * класс инкапсулирующий объект команды help
 */
public class Help implements Command{
    /**
     * Поле для связи с объектом приемника класса {@link Receiver} выполняющим команду
     */
    private final Receiver receiver;
    /**
     * Ссылка на коллекцию в которой хранятся все доступные пользователю команды
     */
    private final Collection<Command> commands;

    /**
     * Стандартный конструктор
     * @param receiver объект приемника
     * @param commands коллекция команд
     */
    public Help(Receiver receiver, Collection<Command> commands){
        this.receiver = receiver;
        this.commands = commands;
    }

    /**
     * Переопределенные методы из интерфейса {@link commands.Command}
     * логика описана в самом интерфейсе
     */
    @Override
    public void execute(List<String> args) {
        if (args != null) throw new IllegalArgumentException("Команда help не принимает никаких аргументов");
        receiver.help(this.commands);
    }

    @Override
    public String description() {
        return "help - справка по доступным командам";
    }
}
