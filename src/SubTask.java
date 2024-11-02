public class SubTask extends Epic {

    public SubTask(String subTask_name, Status status) {
        super(subTask_name, status);
    }

    public void printSubTask(int num){
        System.out.println("\tПодзадача №" + num +": " + getTaskName() + "\n\tСтатус: " + status);
    }

}