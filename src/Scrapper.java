import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import java.util.ArrayList;

class Element {
    public int size;
    public WebElement element;
    public ArrayList<Integer> index;
    public ArrayList<Double> w;
    public ArrayList<Double> t;
    public Element(int size, WebElement wb, boolean writeOut) {
        this.size = size;
        this.element = wb;
        index = new ArrayList<>();
        w = new ArrayList<>();
        t = new ArrayList<>();
        extractWB();
        if (writeOut) {
            System.out.println(this);
        }
    }
    private void extractWB(){
        int n=0;
        while(n<size){
            index.add(Integer.valueOf(element.getText().split("\n")[n].split(" ")[0]));
            w.add(Double.valueOf(element.getText().split("\n")[n].split(" ")[1]));
            t.add(Double.valueOf(element.getText().split("\n")[n].split(" ")[2]));
            n++;
        }
    }
    @Override
    public String toString() {
        return "size: " + size + "\nindex: " + index + "\nw: " + w + "\nt: " + t + "\n";
    }
}

public class Scrapper {
    WebDriver driver;
    ArrayList<Element> list;
    ArrayList<WebElement> tables;
    final String url = "https://pomax.github.io/bezierinfo/legendre-gauss.html";
    public int n;
    public Scrapper(int n, boolean writeOut) {
        if(n > 64){
            throw new IllegalArgumentException("Scrapper only supports 64 elements");
        }
        System.setProperty("webdriver.chrome.driver", "chromedriver-win64/chromedriver.exe");
        this.tables = new ArrayList<>();
        this.list = new ArrayList<>();
        this.n = n;

        driver = new ChromeDriver();
        driver.manage().window().minimize();
        driver.get(url);

        findMainTable();
        extractElements(writeOut);

        driver.close();
    }
    Element getElement(int size){
        return list.get(size - 2);
    }
    private void findMainTable(){
        for(int i = 2; i < n + 1; i++){
            WebElement element = driver.findElement(By.id("n" + i));
            element = element.findElement(By.className("tbl"));
            element = element.findElement(By.tagName("tbody"));
            tables.add(element);
        }
    }
    private void extractElements(boolean writeOut){
        for(int i = 0; i < tables.size(); i++){
            list.add(new Element(i+2,tables.get(i),writeOut));
        }
    }
}
