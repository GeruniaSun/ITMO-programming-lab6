package lt.shgg.commands;

import lt.shgg.app.Receiver;
import lt.shgg.data.Ticket;

import java.util.Arrays;
import java.util.List;

/**
 * <h1>Команда count_greater_than_type</h1>
 * класс инкапсулирующий объект команды count_greater_than_type
 */
public class CountGreaterThanType implements Command{
    /**
     * Поле для связи с объектом приемника класса {@link Receiver} выполняющим команду
     */
    private final Receiver receiver;

    /**
     * Стандартный конструктор
     * @param receiver объект приемника
     */
    public CountGreaterThanType(Receiver receiver){
        this.receiver = receiver;
    }

    /**
     * Переопределенные методы из интерфейса {@link commands.Command}
     * логика описана в самом интерфейсе
     */
    @Override
    public void execute(List<String> args) {
        if (args == null || args.isEmpty())
            throw new NullPointerException("Команда count_greater_than_type не работает без аргумента type");
        if (args.size() > 1)
            throw new IllegalArgumentException("Команда count_greater_than_type принимает только 1 аргумент");
        Ticket.TicketType type;
        try {
            type = Ticket.TicketType.valueOf(args.get(0).toUpperCase());
        } catch (Exception e) {
            throw new IllegalArgumentException("типа " + args.get(0) + " не существует, " +
                    "пожалуйста введите один из нижеприведенных типов\n" + Arrays.toString(Ticket.TicketType.values()));
        }
        receiver.countGreaterThanType(type);
    }

    @Override
    public String description() {
        return "count_greater_than_type type - выводит количество элементов коллекции, " +
                "значение поля type которых больше заданного";
    }
}
