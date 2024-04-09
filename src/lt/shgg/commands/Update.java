package lt.shgg.commands;

import lt.shgg.app.Receiver;

import java.util.List;

/**
 * <h1>Команда update</h1>
 * класс инкапсулирующий объект команды update
 */
public class Update implements Command{
    /**
     * Поле для связи с объектом приемника класса {@link Receiver} выполняющим команду
     */
    private final Receiver receiver;

    /**
     * Стандартный конструктор
     * @param receiver объект приемника
     */
    public Update(Receiver receiver){
        this.receiver = receiver;
    }

    /**
     * Переопределенные методы из интерфейса {@link commands.Command}
     * логика описана в самом интерфейсе
     */
    @Override
    public void execute(List<String> args) {
        if (this.receiver.isConsoleFlag() && args == null || args.isEmpty())
            throw new NullPointerException("Команда update не работает без аргумента id");
        if (this.receiver.isConsoleFlag() && args.size() > 1)
            throw new IllegalArgumentException("Команда update принимает только 1 аргумент " +
                    "(поля элемента нужно вводить со следующей строки)");
        long id;
        try {
            id = Long.parseLong(args.get(0).toUpperCase());
        } catch (Exception e) {
            throw new IllegalArgumentException("аргумент id должен быть числом");
        }
        receiver.update(id, args.subList(1, args.size()));
    }

    @Override
    public String description() {
        return "update id {element} - обновляет поля элемента по его id";
    }
}