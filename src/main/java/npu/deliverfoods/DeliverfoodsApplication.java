package npu.deliverfoods;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DeliverfoodsApplication {

	public static void main(String[] args) {
		SpringApplication.run(DeliverfoodsApplication.class, args);
		System.setProperty("hsqldb.method_class_names", "net.ucanaccess.*");
	}

}
