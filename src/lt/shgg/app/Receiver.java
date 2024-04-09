package lt.shgg.app;

import lt.shgg.commands.Command;
import lt.shgg.data.*;
import lt.shgg.parsing.Parser;

import java.io.*;
import java.nio.file.AccessDeniedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

/**
 * <h1>Класс приемник</h1>
 * экземпляр класса содержит методы выполняющие всю логику команд и некоторые служебные
 */
public class Receiver {
    /**
     * Коллекция, которой манипулирует приложение
     */
    private final Collection<Ticket> data;
    /**
     * Имя файла, которое было передано в качестве аргумента при запуске приложения
     */
    private final String filename;
    /**
     * Текущий поток ввода, по умолчанию системный ввод, но может смениться на файловый
     */
    private InputStreamReader inputStreamReader = new InputStreamReader(System.in);
    /**
     * Поле которое хранит предыдущее значения поля inputStreamReader
     */
    private InputStreamReader lastInputStreamReader = new InputStreamReader(System.in);
    /**
     * Флаг истинный, если в данный момент ввод происходит с консоли, ложный в противном случае
     */
    private boolean consoleFlag = true;
    /**
     * Поле в которое записываются файлы во время выполнения команды execute_script
     * нужно для защиты от рекурсивных вызовов в скриптах
     */
    private final Set<Path> recursionDefense = new HashSet<>();

    /**
     * Стандартный конструктор
     * @param data коллекция экземпляров класса {@link Ticket}
     * @param filename имя файла, которое было передано в качестве аргумента при запуске приложения
     */
    public Receiver(Storage data, String filename) {
        this.data = data.collection();
        this.filename = filename;
    }

    /**
     * Метод соответствующий команде help<br>
     * команда help выводит список доступных команд и краткое описание их действия
     * @param commands список всех доступных пользователю команд
     */
    public void help(Collection<Command> commands){
        System.out.println("вот список доступных вам команд: ");
        commands.forEach(cmd -> System.out.println(cmd.description()));
    }

    /**
     * Метод соответствующий команде info<br>
     * команда info выводит информацию о коллекции
     */
    public void info(){
        System.out.println("Тип коллекции: " + data.getClass().getSimpleName());
        System.out.println("Размер коллекции: " + data.size());
    }

    /**
     * Метод соответствующий команде show<br>
     * команда show выводит всю коллекцию
     */
    public void show(){
        if (data.isEmpty()) System.out.println("Коллекция пуста. Используйте команду add, чтоб добавить элементы");
        data.forEach(System.out::println);
    }

    /**
     * Метод соответствующий команде exit<br>
     * команда exit закрывает приложение
     */
    public void exit(){
        System.out.println("Выход из приложения...");
        System.exit(666);
    }

    /**
     * Метод соответствующий команде save<br>
     * команда save сохраняет коллекцию в файл переданный в качестве аргумента при запуске приложения
     */
    public void save(){
        try {
            Parser.saveToFile(new File(filename),data);
            System.out.println("Коллекция сохранена успешно");
        } catch (IOException e) {
            System.out.println("Ошибка во время сохранения коллекции в файл. Возможно файл перемещен или удален");
        }
    }

    /**
     * Служебный метод для создания нового экземпляра класса {@link Ticket}
     * введенного пользователем с консоли<br>
     * не является доступной пользователю командой!!!
     * @param builder объект строителя билета
     * @return новоиспеченный билет
     */
    private Ticket create(TicketBuilder builder){
        var in = new Scanner(new BufferedReader(this.inputStreamReader));

        while (this.consoleFlag || in.hasNextLine()) {
            try {
                System.out.println("Введите название");
                String input = in.nextLine();
                builder.withName(input);
                break;
            } catch (NullPointerException e) {
                System.out.println(e.getMessage());
            } catch (NoSuchElementException e) {
                System.out.println("вы какие-то гадости делаете. Закрываю приложение");
                System.exit(999);
            }
        }

        while (this.consoleFlag || in.hasNextLine()) {
            try {
                System.out.println("Введите координаты через пробел");
                String[] input = in.nextLine().split(" ");
                float x = Float.parseFloat(input[0]);
                int y = Integer.parseInt(input[1]);
                try{
                    if (input.length > 2) throw new IllegalArgumentException("Введено слишком много аргументов");
                    builder.withCoordinates(new Coordinates(x, y));
                    break;
                } catch (NullPointerException | IllegalArgumentException e) {
                    System.out.println(e.getMessage());
                }
            } catch (NoSuchElementException e) {
                System.out.println("вы какие-то гадости делаете. Закрываю приложение");
                System.exit(999);
            }
            catch (Exception e){
                System.out.println("Это не подойдет для значения координат");
            }
        }

        while (this.consoleFlag || in.hasNextLine()) {
            try {
                System.out.println("Введите цену");
                String input = in.nextLine();
                var price = Long.parseLong(input);
                try {
                    builder.withPrice(price);
                    break;
                } catch (NullPointerException | IllegalArgumentException e) {
                    System.out.println(e.getMessage());
                }
            } catch (NullPointerException | IllegalArgumentException e) {
                System.out.println("Это не подойдет для значения цены");
            } catch (NoSuchElementException e) {
                System.out.println("вы какие-то гадости делаете. Закрываю приложение");
                System.exit(999);
            }
        }

        while (this.consoleFlag || in.hasNextLine()) {
            try {
                System.out.println("Введите один из нижеприведенных типов билета");
                System.out.println(Arrays.toString(Ticket.TicketType.values()));
                String input = in.nextLine().toUpperCase();
                var type = Ticket.TicketType.valueOf(input);
                builder.withType(type);
                break;
            }
            catch (IllegalArgumentException e){
                System.out.println("Такого варианта нет");
            } catch (NoSuchElementException e) {
                System.out.println("вы какие-то гадости делаете. Закрываю приложение");
                System.exit(999);
            }
        }

        var venueFlag = false;
        while (this.consoleFlag || in.hasNextLine()) {
            System.out.println("Вы хотите добавить место проведения?");
            System.out.println("Введите YES/NO");
            var answer = in.nextLine();
            if (answer.equalsIgnoreCase("NO")) break;
            else if (answer.equalsIgnoreCase("YES")) {
                venueFlag = true;
                break;
            } else {
                System.out.println("Пожалуйста, введите 'YES' или 'NO'");
            }
        }

        if (venueFlag) {
            var venueBuilder = new VenueBuilder();

            while (this.consoleFlag || in.hasNextLine()) {
                try {
                    System.out.println("Введите название места проведения");
                    String input = in.nextLine();
                    venueBuilder.withName(input);
                    break;
                } catch (NoSuchElementException e) {
                    System.out.println("вы какие-то гадости делаете. Закрываю приложение");
                    System.exit(999);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }

            while (this.consoleFlag || in.hasNextLine()) {
                try {
                    System.out.println("Введите вместимость места проведения");
                    String input = in.nextLine();
                    var capacity = Integer.parseInt(input);
                    try {
                        venueBuilder.withCapacity(capacity);
                        break;
                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                    }
                } catch (NoSuchElementException e) {
                    System.out.println("вы какие-то гадости делаете. Закрываю приложение");
                    System.exit(999);
                } catch (Exception e) {
                    System.out.println("это не подойдет для вместимости");
                }
            }

            var addressFlag = false;
            while (this.consoleFlag || in.hasNextLine()) {
                System.out.println("Вы хотите добавить адрес места проведения?");
                System.out.println("Введите YES/NO");
                var answer = in.nextLine();
                if (answer.equalsIgnoreCase("NO")) break;
                else if (answer.equalsIgnoreCase("YES")) {
                    addressFlag = true;
                    break;
                } else System.out.println("Пожалуйста, введите 'YES' или 'NO'");
            }

            if (addressFlag){
                while (this.consoleFlag || in.hasNextLine()) {
                    try {
                        System.out.println("Введите адрес места проведения");
                        String input = in.nextLine();
                        venueBuilder.withAddress(new Venue.Address(input));
                        break;
                    } catch (NoSuchElementException e) {
                        System.out.println("вы какие-то гадости делаете. Закрываю приложение");
                        System.exit(999);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }
            }

            builder.withVenue(venueBuilder.build());
        }

        if (builder.isReadyToBuild()){
            return builder.build();
        } else throw new IllegalStateException("Что-то пошло не так во время описания билета");
    }

    /**
     * Служебный метод для создания нового экземпляра класса {@link Ticket}
     * введенного пользователем в файле<br>
     * не является доступной пользователю командой!!!
     * @param builder объект строителя билета
     * @return новоиспеченный билет
     */
    private Ticket createFromFile(TicketBuilder builder, List<String> args){
        try {
            String input = String.join(" ", args);
            ArrayList<String> values = new ArrayList<>(List.of(input.substring(1)
                    .replace("}", "").split(", ")));
            values.addAll(List.of("", "", ""));
            builder.withName(values.get(0).substring(1).replace("'", ""));
            builder.withCoordinates(new Coordinates(Float.parseFloat(values.get(1)), Integer.parseInt(values.get(2))));
            builder.withPrice(Long.parseLong(values.get(3)));
            System.out.println("4");

            builder.withType(Ticket.TicketType.valueOf(values.get(4)));
            if (!values.get(5).contains("null")) {
                var venueBuilder = new VenueBuilder();
                venueBuilder.withName(values.get(5).substring(1).replace("'", ""));
                venueBuilder.withCapacity(Integer.parseInt(values.get(6)));
                if (!values.get(7).contains("null"))
                    venueBuilder.withAddress(new Venue.Address(values.get(7)
                            .substring(1).replace("'", "")));
                builder.withVenue(venueBuilder.build());
            }

            if (builder.isReadyToBuild()) return builder.build();
            else throw new IllegalArgumentException("что-то не так со значениями полей билета");
        } catch (Exception e){
            throw new IllegalStateException("что-то пошло не так во время описания билета");
        }
    }

    /**
     * Метод соответствующий команде add<br>
     * команда add добавляет новый билет в коллекцию
     */
    public void add(List<String> args){
        var builder = new TicketBuilder();
        try {
            if (consoleFlag){
                data.add(this.create(builder));
            } else data.add(this.createFromFile(builder, args));
            System.out.println("Вы успешно добавили билет");
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage() + " команда add не выполнена");
        }
    }

    /**
     * Метод соответствующий команде add_if_max<br>
     * команда add_if_max добавляет новый билет в коллекцию, если он превосходит максимальный в коллекции
     */
    public void addIfMax(List<String> args){
        try {
            var builder = new TicketBuilder();
            Ticket noob;
            if (consoleFlag){
                noob = this.create(builder);
            } else noob = this.createFromFile(builder, args);
            if (noob.compareTo(Collections.max(data)) > 0) data.add(noob);
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage() + " команда add_if_max не выполнена");
        }
    }

    /**
     * Метод соответствующий команде remove_greater<br>
     * команда remove_greater удаляет из коллекции все элементы, большие введенного
     */
    public void removeGreater(List<String> args){
        try {
            var builder = new TicketBuilder();
            Ticket compared;
            if (consoleFlag){
                compared = this.create(builder);
            } else compared = this.createFromFile(builder, args);
            data.removeIf(ticket -> ticket.compareTo(compared) > 0);
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage() + " команда remove_greater не выполнена");
        }
    }

    /**
     * Метод соответствующий команде remove_lower<br>
     * команда remove_lower удаляет из коллекции все элементы, меньшие введенного
     */
    public void removeLower(List<String> args){
        try {
            var builder = new TicketBuilder();
            Ticket compared;
            if (consoleFlag){
                compared = this.create(builder);
            } else compared = this.createFromFile(builder, args);
            data.removeIf(ticket -> ticket.compareTo(compared) < 0);
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage() + " команда remove_lower не выполнена");
        }
    }

    /**
     * Метод соответствующий команде print_descending<br>
     * команда print_descending выводит все элементы коллекции в порядке убывания
     */
    public void printDescending() {
        data.stream().sorted(Comparator.reverseOrder()).forEach(System.out::println);
    }

    /**
     * Метод соответствующий команде filter_by_type<br>
     * команда filter_by_type выводит все элементы коллекции с типом type
     * @param type один из типов {@link Ticket.TicketType}
     */
    public void filterByType(Ticket.TicketType type){
        data.stream().filter(ticket -> ticket.getType() == type).forEach(System.out::println);
    }

    /**
     * Метод соответствующий команде count_greater_than_type<br>
     * команда count_greater_than_type выводит количество элементов коллекции с типом type
     * @param type один из типов {@link Ticket.TicketType}
     */
    public void countGreaterThanType(Ticket.TicketType type){
        System.out.println(data.stream().filter(ticket -> ticket.getType().compareTo(type) > 0).count());
    }

    /**
     * Метод соответствующий команде clear<br>
     * команда clear очищает коллекцию
     */
    public void clear(){
        data.clear();
        System.out.println("Коллекция успешно очищена");
    }

    /**
     * Метод соответствующий команде remove_by_id<br>
     * команда remove_by_id удаляет из коллекции элемент по его id,
     * в случае отсутствия элемента с нужным id коллекция не меняется
     * @param id искомое id
     */
    public void removeById(Long id){
        var success = data.removeIf(ticket -> ticket.getId().equals(id));
        if (success) System.out.println("Элемент успешно удалён");
        else System.out.println("В коллекции нет элемента с id " + id);
    }

    /**
     * Метод соответствующий команде update<br>
     * команда update обновляет значения полей элемента по id,
     * в случае отсутствия элемента с нужным id коллекция не меняется
     * @param id искомое id
     */
    public void update(Long id, List<String> args){
        Ticket oldTicket = null;
        for (Ticket ticket : data){
            if (ticket.getId().equals(id)){
                oldTicket = ticket;
                break;
            }
        }
        if (oldTicket == null) System.out.println("Элемента с таким id не существует");
        else {
            try {
                var builder = new TicketBuilder();
                builder.withId(oldTicket.getId());
                if (consoleFlag){
                    data.remove(oldTicket);
                    data.add(this.create(builder));
                } else data.add(this.createFromFile(builder, args));
                System.out.println("Значения полей элемента успешно обновлены");
            } catch (IllegalStateException e) {
                System.out.println(e.getMessage() + "команда update не выполнена");
            }
        }
    }

    /**
     * Метод соответствующий команде execute_script<br>
     * команда execute_script выполняет все команды из переданного файла<br>
     * рекурсивные вызовы запрещены!<br>
     * при командах с ошибками выводятся соответствующие сообщения об ошибках
     * @param filename имя файла скрипта
     * @throws AccessDeniedException если у программы нет прав, чтоб прочитать файл
     * @throws FileNotFoundException если файла не существует
     * @throws IllegalArgumentException при обнаружении рекурсивного вызова
     */
    public void executeScript(String filename) throws AccessDeniedException, FileNotFoundException {
        var path = Path.of(filename).toAbsolutePath();
        if (!Files.isReadable(path))
            throw new AccessDeniedException("Такого файла не существует или у программы нет прав, чтоб его прочитать");
        if (this.recursionDefense.contains(path))
            throw new IllegalArgumentException("в ваших скриптах обнаружена рекурсия, а рекурсия вредна для здоровья!");
        this.recursionDefense.add(path);
        var fileIn = new FileReader(path.toFile());
        this.setInputStreamReader(fileIn);
        this.setConsoleFlag(false);
        var fileInvoker = new Invoker(this);
        var fileConsole = new AppConsole();
        System.out.println("начинаю читать ваш скрипт...");
        fileConsole.runApp(fileIn, fileInvoker);
        System.out.println("достигнут конец файла " + filename);
        this.setInputStreamReader(this.lastInputStreamReader);
        this.setConsoleFlag(true);
        this.recursionDefense.clear();
    }

    /**
     * Метод для смены потока ввода
     * @param inputStreamReader новый поток ввода (стандартный или файловый)
     */
    public void setInputStreamReader(InputStreamReader inputStreamReader) {
        this.lastInputStreamReader = this.inputStreamReader;
        this.inputStreamReader = inputStreamReader;
    }

    /**
     * Метод для проверки консольного флага
     * @return истину если ввод происходит с консоли и ложь в противном случае
     */
    public boolean isConsoleFlag() {
        return consoleFlag;
    }

    /**
     * Метод для изменения консольного флага
     * @param consoleFlag флаг истинный, если ввод происходит с консоли и ложный в противном случае
     */
    public void setConsoleFlag(boolean consoleFlag) {
        this.consoleFlag = consoleFlag;
    }
}
