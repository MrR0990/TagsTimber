<a name="KHf5P"></a>
## 目标
从复杂逻辑日志链条中抽离出单个逻辑链条日志,减少日志干扰,基于[https://github.com/JakeWharton/timber](https://github.com/JakeWharton/timber)日志框架改造

<a name="blSoQ"></a>
## 执行过程日志存在交叉混淆

---

假设有以下场景,运行logical1(),logical2(),logical3()三个逻辑,每个method中都添加打印,代码如下

```java
public class DemoActivity2 extends Activity {
    private static final String TAG = "DemoActivity";

    private static final String TAG_LOGICAL2 = "TAG_LOGICAL2";
    private static final String TAG_LOGICAL3 = "TAG_LOGICAL3";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DemoActivityBinding binding = DemoActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        logical1();
        logical2();
        logical3();
    }


    //实际执行步骤是method1-5
    void logical1() {
        method1();
    }

    //实际执行步骤是method2-5
    void logical2() {
        method2();
    }

    //实际执行步骤是method3-5
    void logical3() {
        method3();
    }


    //Method
    private void method1() {
        Log.d(TAG, "method1: ");
        method2();
    }

    private void method2() {
        Log.d(TAG, "method2: ");
        Log.d(TAG_LOGICAL2, "method2: ");
        method3();
    }

    private void method3() {

        Log.d(TAG, "method3: ");
        Log.d(TAG_LOGICAL2, "method3: ");
        Log.d(TAG_LOGICAL3, "method3: ");
        method4();
    }

    private void method4() {

        Log.d(TAG, "method4: ");
        Log.d(TAG_LOGICAL2, "method4: ");
        Log.d(TAG_LOGICAL3, "method4: ");
        method5();

    }

    private void method5() {

        Log.d(TAG, "method5: ");
        Log.d(TAG_LOGICAL2, "method5: ");
        Log.d(TAG_LOGICAL3, "method5: ");
    }

}

```
**运行结果如下,此时过滤TAG_LOGICAL2的日志比较混乱,并且不是很准确,期间掺杂logical3()执行步骤的日志.**
```java
2022-02-25 14:17:27.712 11026-11026/com.example.timber D/DemoActivity: method1: 
2022-02-25 14:17:27.713 11026-11026/com.example.timber D/DemoActivity: method2: 
2022-02-25 14:17:27.713 11026-11026/com.example.timber D/TAG_LOGICAL2: method2: 
2022-02-25 14:17:27.713 11026-11026/com.example.timber D/DemoActivity: method3: 
2022-02-25 14:17:27.713 11026-11026/com.example.timber D/TAG_LOGICAL2: method3: 
2022-02-25 14:17:27.713 11026-11026/com.example.timber D/TAG_LOGICAL3: method3: 
2022-02-25 14:17:27.713 11026-11026/com.example.timber D/DemoActivity: method4: 
2022-02-25 14:17:27.713 11026-11026/com.example.timber D/TAG_LOGICAL2: method4: 
2022-02-25 14:17:27.713 11026-11026/com.example.timber D/TAG_LOGICAL3: method4: 
2022-02-25 14:17:27.713 11026-11026/com.example.timber D/DemoActivity: method5: 
2022-02-25 14:17:27.713 11026-11026/com.example.timber D/TAG_LOGICAL2: method5: 
2022-02-25 14:17:27.713 11026-11026/com.example.timber D/TAG_LOGICAL3: method5: 
2022-02-25 14:17:27.713 11026-11026/com.example.timber D/DemoActivity: method2: 
2022-02-25 14:17:27.713 11026-11026/com.example.timber D/TAG_LOGICAL2: method2: 
2022-02-25 14:17:27.713 11026-11026/com.example.timber D/TAG_LOGICAL3: method2: 
2022-02-25 14:17:27.713 11026-11026/com.example.timber D/DemoActivity: method3: 
2022-02-25 14:17:27.713 11026-11026/com.example.timber D/TAG_LOGICAL2: method3: 
2022-02-25 14:17:27.713 11026-11026/com.example.timber D/TAG_LOGICAL3: method3: 
......
```

<a name="wlBYk"></a>
## 如何进行优化?
> 使用仓库: [https://github.com/MrR0990/TagsTimber](https://github.com/MrR0990/TagsTimber)

```java
DemoActivity.java

public class DemoActivity extends Activity {
    private static final String TAG = "DemoActivity";

    private static final String TAG_LOGICAL2 = "TAG_LOGICAL2";
    private static final String TAG_LOGICAL3 = "TAG_LOGICAL3";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DemoActivityBinding binding = DemoActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        logical1();
        logical2();
        logical3();
    }


  
    void logical1() {
        method1();
    }

   
    void logical2() {
        Timber.start(TAG_LOGICAL2);//开始记录TAG_LOGICAL2的日志
        method2();
    }

 
    void logical3() {
        Timber.start(TAG_LOGICAL3);//开始记录TAG_LOGICAL3的日志
        method3();
    }


   
    private void method1() {
        Timber.d("method1: ");
        method2();
    }

    private void method2() {
      
        Timber.tags(TAG_LOGICAL2).d("method2: ");//打印TAG_LOGICAL2的日志
        method3();
    }

    private void method3() {
       
        Timber.tags(TAG_LOGICAL2, TAG_LOGICAL3).d("method3: ");////同时记录TAG_LOGICAL2和TAG_LOGICAL3的日志
        method4();
    }

    private void method4() {
        Timber.tags(TAG_LOGICAL2, TAG_LOGICAL3).d("method4: ");
        method5();

    }

    private void method5() {
        Timber.tags(TAG_LOGICAL2, TAG_LOGICAL3).d("method5: ");
        Timber.end(TAG_LOGICAL2, TAG_LOGICAL3);//结束TAG_LOGICAL2和TAG_LOGICAL3的日志
    }

}

```
在ExampleApp进行全局的配置
```java
ExampleApp.java

public class ExampleApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Timber.plant(new DebugTree());//初始化框架
        Timber.openTags(TAG_LOGICAL3);//只查看TAG_LOGICAL3的日志
    }

}
```


**运行结果**
```java
2022-03-18 18:14:54.525 22498-22498/com.example.timber D/DemoActivity: method1: 
2022-03-18 18:14:54.527 22498-22498/com.example.timber D/DemoActivity: method2: 
2022-03-18 18:14:54.528 22498-22498/com.example.timber D/DemoActivity: method3: 
2022-03-18 18:14:54.529 22498-22498/com.example.timber D/DemoActivity: method4: 
2022-03-18 18:14:54.530 22498-22498/com.example.timber D/DemoActivity: method5: 
2022-03-18 18:14:54.531 22498-22498/com.example.timber D/DemoActivity: method2: 
2022-03-18 18:14:54.532 22498-22498/com.example.timber D/DemoActivity: method3: 
2022-03-18 18:14:54.534 22498-22498/com.example.timber D/DemoActivity: method4: 
2022-03-18 18:14:54.535 22498-22498/com.example.timber D/DemoActivity: method5: 
2022-03-18 18:14:54.536 22498-22498/com.example.timber D/DemoActivity: method3: 
2022-03-18 18:14:54.537 22498-22498/com.example.timber D/DemoActivity[TAG_LOGICAL3]: method3: 
2022-03-18 18:14:54.538 22498-22498/com.example.timber D/DemoActivity: method4: 
2022-03-18 18:14:54.538 22498-22498/com.example.timber D/DemoActivity[TAG_LOGICAL3]: method4: 
2022-03-18 18:14:54.539 22498-22498/com.example.timber D/DemoActivity: method5: 
2022-03-18 18:14:54.540 22498-22498/com.example.timber D/DemoActivity[TAG_LOGICAL3]: method5: 

```

- 非常清晰看到logical3()执行步骤的日志,
- 执行logical3()时调用了end(TAG_LOGICAL2)之后,在logical3()步骤中,不会重复打印logical2()的日志
- 在众多复杂日志中可以非常清晰的显示出logical3()的日志以及logical3()的执行逻辑

**切换调试的逻辑,在全局配置中,使用Timber.openTags(),即可切换想要调试的逻辑日志**
```java
ExampleApp.java

Timber.openTags(TAG_LOGICAL3);//只查看TAG_LOGICAL3的日志
//Timber.openTags(TAG_LOGICAL2,TAG_LOGICAL3);//同时查看TAG_LOGICAL2,TAG_LOGICAL3的日志
```

**特别说明:**<br />start()和end()方法只生效于当前线程,如果切换了线程,在另一线程也需要调用start()和end()伪代码如下
```java

Timber.start(TAG_LOGICAL2);

......

Timber.tags(TAG_LOGICAL2, TAG_LOGICAL3).d("method");

new Thread(new Runnable(){
   void run(){
  
	Timber.start(TAG_LOGICAL2);//新的线程也需要调用Start()方法
	......
	Timber.tags(TAG_LOGICAL2, TAG_LOGICAL3).d("method");
	......
	Timber.end(TAG_LOGICAL2);

 }
           
})

......

Timber.end(TAG_LOGICAL2);


```


