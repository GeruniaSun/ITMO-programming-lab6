package lt.shgg.data;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.time.LocalDate;
import java.util.Objects;

/**
 * <h1>Класс описывающий сущность билета</h1>
 * билеты - сущности для управления коллекцией коих, существует это приложение
 */
public class Ticket implements Comparable<Ticket> {
    /**
     * Уникальный положительный номер билета в системе, значение этого поля генерируется автоматически
     */
    @JacksonXmlProperty
    private Long id;
    /**
     * Название мероприятия на которое приобретен билет<br>
     * это поле не может быть null или пустой строкой
     */
    @JacksonXmlProperty
    private String name;
    /**
     * Координаты места проведения мероприятия<br>
     * представляются объектом специального класса Coordinates {@link data.Coordinates}<br>
     * это поле не может быть null
     */
    @JacksonXmlProperty
    private Coordinates coordinates;
    /**
     * Дата создания объекта билета, значение этого поля генерируется автоматически
     */
    private LocalDate creationDate;
    /**
     * Цена билета<br>
     * это поле не может быть null, значение поля должно быть неотрицательным
     */
    @JacksonXmlProperty
    private Long price;
    /**
     * Тип билета принадлежащий перечислению {@link data.Ticket.TicketType}<br>
     * это поле не может быть null
     */
    @JacksonXmlProperty
    private TicketType type;
    /**
     * Объект места проведения мероприятия представлен экземпляром специального класса {@link data.Venue}<br>
     * это поле может быть null
     */
    @JacksonXmlProperty
    private Venue venue;

    /**
     * Перечисление описывающее типы билетов
     */
    public enum TicketType {
        CHEAP,
        BUDGETARY,
        USUAL,
        VIP;
    }

    /**
     * Стандартный конструктор для корректной работы парсера XML
     */
    private Ticket(){}

    /**
     * Стандартный метод для получения значения поля id
     * @return значения поля id
     */
    public Long getId() {
        return id;
    }
    /**
     * Стандартный метод для получения значения поля type
     * @return значения поля type
     */
    public TicketType getType() {
        return type;
    }

    /**
     * Конструктор вызываемый классом строителем {@link data.TicketBuilder} для создания нового билета
     * @param id значение для поля id
     * @param name значение для поля name
     * @param coordinates значение для поля coordinates
     * @param creationDate значение для поля creationDate
     * @param price значение для поля price
     * @param type значение для поля type
     * @param venue значение для поля venue
     */
    public Ticket(Long id, String name, Coordinates coordinates, LocalDate creationDate,
                  Long price, TicketType type, Venue venue) {
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = creationDate;
        this.price = price;
        this.type = type;
        this.venue = venue;
    }

    /**
     * Стандартный метод для строкового представления объекта
     * @return строку включающую значения всех полей объекта
     */
    @Override
    public String toString() {
        return "Ticket{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", coordinates=" + coordinates +
                ", creationDate=" + creationDate +
                ", price=" + price +
                ", type=" + type +
                ", venue=" + venue +
                '}';
    }

    /**
     * Стандартный метод из класса Object для сравнения объектов
     * @param otherObject сравниваемый объект
     * @return истину в случае эквивалентности объектов, ложь в противном случае
     */
    @Override
    public boolean equals(Object otherObject) {
        if (this == otherObject) return true;
        if (otherObject == null || this.getClass() != otherObject.getClass()) return false;
        Ticket other = (Ticket) otherObject;
        return Objects.equals(id, other.id) && Objects.equals(name, other.name)
                && Objects.equals(coordinates, other.coordinates) && Objects.equals(creationDate, other.creationDate)
                && Objects.equals(price, other.price) && type == other.type && Objects.equals(venue, other.venue);
    }

    /**
     * Стандартный метод из класса Object для вычисления хеш-кода
     * @return хеш-код объекта
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, name, coordinates, creationDate, price, type, venue);
    }

    /**
     * Метод для сортировки билетов по умолчанию<br>
     * сравнение происходит по цене
     * @param other сравниваемый объект билета
     * @return разницу цен, нулевую в случае равенства,
     * отрицательную, если other дороже, положительную в противном случае
     */
    @Override
    public int compareTo(Ticket other) {
        return this.price.compareTo(other.price);
    }
}