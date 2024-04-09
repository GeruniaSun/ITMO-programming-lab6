package lt.shgg.commands;

import lt.shgg.app.Receiver;

import java.util.List;

/**
 * <h1>Команда clear</h1>
 * класс инкапсулирующий объект команды clear
 */
public class Clear implements Command{
    private final Receiver receiver;

    /**
     * Стандартный конструктор
     * @param receiver объект приемника
     */
    public Clear(Receiver receiver){
        this.receiver = receiver;
    }

    /**
     * Переопределенные методы из интерфейса {@link commands.Command}
     * логика описана в самом интерфейсе
     */
    @Override
    public void execute(List<String> args) {
        if (args != null) throw new IllegalArgumentException("Команда clear не принимает никаких аргументов");
        receiver.clear();
    }

    @Override
    public String description() {
        return "clear - удаляет все элементы из коллекции";
    }
}
