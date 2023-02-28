package product.client_final.main;

public class Inventory {
    private String TicketID;
    private String
            Price,
            DiscountPrice,
            FlightCode,
            IssueDate,
            ExpireDate,
            Airline_Name,
            Seat_No,
            FromWhere,
            FromCode,
            ToWhere,
            ToCode,
            DepartureTime,
            DepartureDate,
            ReachingTime,
            ReachingDate,
            Gate,
            Terminal;
    private String imgSrc;

    public void TicketInfo_setter(String TicketID, String FlightCode,
                                  String IssueDate, String ExpireDate,
                                  String Airline_Name, String Seat_No, String FromWhere,
                                  String FromCode, String ToWhere, String ToCode,
                                  String DepartureTime, String DepartureDate, String ReachingTime,
                                  String ReachingDate, String Gate, String Terminal ,String Price, String DiscountPrice)
    {
        this.TicketID = TicketID;
        this.Price = Price;
        this.DiscountPrice = DiscountPrice;
        this.FlightCode = FlightCode;
        this.IssueDate = IssueDate;
        this.ExpireDate = ExpireDate;
        this.Airline_Name = Airline_Name;
        this.Seat_No = Seat_No;
        this.FromWhere = FromWhere;
        this.FromCode= FromCode;
        this.ToWhere = ToWhere;
        this.ToCode = ToCode;
        this.DepartureTime = DepartureTime;
        this.DepartureDate = DepartureDate;
        this.ReachingTime = ReachingTime;
        this.ReachingDate = ReachingDate;
        this.Gate = Gate;
        this.Terminal = Terminal;
    }

    public void setImgSrc(String imgSrc) {
        this.imgSrc = imgSrc;
    }
    public String getImgSrc()
    {
        return this.imgSrc;
    }

    public String getAirLine_Name()
    {
        return this.Airline_Name;
    }
    public String getDiscountPrice()
    {
        return this.DiscountPrice;
    }
    public String getTicketID()
    {
       return this.TicketID;
    }
}
