package lt.shgg.commands;

import lt.shgg.app.Receiver;
import lt.shgg.data.Ticket;

import java.util.Arrays;
import java.util.List;

/**
 * <h1>Команда filter_by_type</h1>
 * класс инкапсулирующий объект команды filter_by_type
 */
public class FilterByType implements Command{
    /**
     * Поле для связи с объектом приемника класса {@link Receiver} выполняющим команду
     */
    private final Receiver receiver;

    /**
     * Стандартный конструктор
     * @param receiver объект приемника
     */
    public FilterByType(Receiver receiver){
        this.receiver = receiver;
    }


    /**
     * Переопределенные методы из интерфейса {@link commands.Command}
     * логика описана в самом интерфейсе
     */
    @Override
    public void execute(List<String> args) {
        if (args == null || args.isEmpty()) throw new NullPointerException("Команда filter_by_type не работает без аргумента type");
        if (args.size() > 1) throw new IllegalArgumentException("Команда filter_by_type принимает только 1 аргумент");
        Ticket.TicketType type;
        try {
            type = Ticket.TicketType.valueOf(args.get(0).toUpperCase());
        } catch (Exception e) {
            throw new IllegalArgumentException("типа " + args.get(0) + " не существует, " +
                    "пожалуйста введите один из нижеприведенных типов\n" + Arrays.toString(Ticket.TicketType.values()));
        }
        receiver.filterByType(type);
    }

    @Override
    public String description() {
        return "filter_by_type type - выводит все элементы коллекции типа type";
    }
}
