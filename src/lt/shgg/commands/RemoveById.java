package lt.shgg.commands;

import lt.shgg.app.Receiver;

import java.util.List;

/**
 * <h1>Команда remove_by_id</h1>
 * класс инкапсулирующий объект команды remove_by_id
 */
public class RemoveById implements Command{
    /**
     * Поле для связи с объектом приемника класса {@link Receiver} выполняющим команду
     */
    private final Receiver receiver;

    /**
     * Стандартный конструктор
     * @param receiver объект приемника
     */
    public RemoveById(Receiver receiver){
        this.receiver = receiver;
    }

    /**
     * Переопределенные методы из интерфейса {@link commands.Command}
     * логика описана в самом интерфейсе
     */
    @Override
    public void execute(List<String> args) {
        if (args == null || args.isEmpty())
            throw new NullPointerException("Команда remove_by_id не работает без аргумента id");
        if (args.size() > 1)
            throw new IllegalArgumentException("Команда remove_by_id принимает только 1 аргумент");
        long id;
        try {
            id = Long.parseLong(args.get(0).toUpperCase());
        } catch (Exception e) {
            throw new IllegalArgumentException("аргумент id должен быть числом");
        }
        receiver.removeById(id);
    }

    @Override
    public String description() {
        return "remove_by_id id - удаляет из коллекции элемент с айди id";
    }
}
