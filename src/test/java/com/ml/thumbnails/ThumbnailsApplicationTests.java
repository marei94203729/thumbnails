package com.ml.thumbnails;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ThumbnailsApplicationTests {

    @Test
    void contextLoads()throws Exception {
        //int MAXIMUM_CAPACITY = 1 << 30;
        //log.info(String.valueOf(MAXIMUM_CAPACITY));
        //  ApplicationContext applicationContext=new AnnotationConfigWebApplicationContext();
        // applicationContext.
        // child c=new child();
        // c.test();
       /* String path="https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=2191785827,4116842063&fm=26&gp=0.jpg";
        URL url=new URL(path);
        Thumbnails.Builder<URL> builder=Thumbnails.of(url);
        builder.scale(1);
        builder.outputQuality(1f);
        BufferedImage img=builder.asBufferedImage();
        Thumbnails.Builder<BufferedImage> builderb=Thumbnails.of(img);
        builderb.scale(0.8);
        builderb.outputQuality(0.1f);
        builderb.toFile(new File("C:\\Users\\hjj\\Desktop\\tt\\21.jpg"));*/

    }

}
