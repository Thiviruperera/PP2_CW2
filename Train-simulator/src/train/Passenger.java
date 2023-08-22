/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package train;

public class Passenger implements Comparable<Passenger> {
    private int id;
    private String firstName;
    private String surname;
    private int secondsInQueue;
    private Integer seatNo;

    public String getName() {
        return firstName + " " + surname;
    }

    public void setName(String firstName, String surname) {
        this.firstName = firstName;
        this.surname = surname;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getSurname() {
        return surname;
    }

    public int getSecondsInQueue() {
        return secondsInQueue;
    }

    public void setSecondsInQueue(int secondsInQueue) {
        this.secondsInQueue = secondsInQueue;
    }

    //display seat object detail
    public void display() {
        System.out.println("=======");
        System.out.println(this.id);
        System.out.println(this.getName());
        System.out.println("=======");
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getSeatNo() {
        return seatNo;
    }

    public void setSeatNo(Integer seatNo) {
        this.seatNo = seatNo;
    }


    @Override
    public int compareTo(Passenger o) {
        return this.getSeatNo().compareTo(o.getSeatNo());
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Passenger other = (Passenger) obj;
        if (!firstName.equals(other.firstName))
            return false;
        if (!surname.equals(other.surname))
            return false;

        return true;
    }
}
