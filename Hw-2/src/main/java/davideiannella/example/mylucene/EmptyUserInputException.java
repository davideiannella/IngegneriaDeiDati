package davideiannella.example.mylucene;

public class EmptyUserInputException extends RuntimeException{
    @Override
    public String toString() {
        return super.toString() + "User Input is empty";
    }
}
