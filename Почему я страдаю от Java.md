# Почему я страдаю от Java

## Система контроля типов которая пропускает ошибки

## Всё делается только через классы, нет структур

*Приводит к перерасходу памяти, выделение в куче, нагрузку на сборщик мусора*

*Что нам дают Value типы (структуры)*
- *Можно размещать на стеке*
- *Хранятся там где определены, например в массиве, в объекте, не требуют отдельного места в куче, с последующей сборкой мусора*

*Например: DTO мы постоянно создаём в большом количестве и передаём дальше*

### Value типы (структуры) есть в C#, F#, Golang, Rust

### Нет Value типов (структуры) Python, Java, PHP

## Нет нормальных Generic

### *Не возможно сделать нормальные generic коллекции которые есть наверное во всех языках где есть generic*

```java
public class MyCollection<T> {
    // Не могу сделать массив типа T!!!
    private T[] buffer;
}
```

### *Вот как это делает C#*

```C#
public class SampleCollection<T>
{
    private T[] arr = new T[100];
}
```

### *Вот как это делает Python*

```python
class SampleCollection[T]:
    items: list[T]

    def __init__(self) -> None:
        self.items = []
```

## Нет Async/Await

### Асинхронность в Java

#### *Асинхронная функция*

```Java
CompletableFuture<Integer> doAsync(){
    return  CompletableFuture.supplyAsync(() -> {
        // Имитация долгой операции
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return 34;
    });
}


// Как использовать
doAsync().thenApply(result -> {
    System.out.println(result);
    return result.toString();
});
```

### *Вот как это делает Rust*

```Rust
async fn do_async() -> i32 {
    // Имитация долгой операции
    sleep(Duration::from_secs(10)).await;
    34 // Возвращаем значение i32
}

// Как использовать
let tt = do_async().await;
println!(tt);
```

### *Вот как это делает C#*

```C#
async Task<int> doAsync()
{
    await Task.Delay(10_000);
    return 34;
}


// Как использовать
var tt = await doAsync();
Console.WriteLine(tt);
```

### *Вот как это делает TS*

```JS
async Promise<number> doAsync()
{
    await new Promise((resolve) => setTimeout(resolve, 10_000))
    return 34;
}


// Как использовать
var tt = await doAsync();
Console.log(tt);
```

### *Вот как это делает Python*

```Python
async def do_async() -> int:
    await asyncio.sleep(10)
    return 34

# Как использовать
tt = await doAsync();
print(tt);
```

### *Языки которые говорят что им не нужен Async/Await - это **Golang***

### *Асинхронная функция*

*Менее удобно чем async, но...*

```go
func doAsync() <-chan int {
 result := make(chan int)

 go func() {
  // Имитация долгой операции
  time.Sleep(10 * time.Second)
  result <- 34
 }()

 return result
}

// Как использовать
fmt.Println("До await")
tt := <-doAsync() // Это прямо  аналог Await
fmt.Println(tt)
```

### Async/Await есть в Kotlin, C#, Rust, F#, JS, TypeScript, PHP, Python

## Нет Генераторов (yield)

*Главная задача генераторов это ленивые вычисления с последующей обработкой. Хорошим примером тут является Linq (что в Java попытались сделать через Stream)*

### *Вот как это делает C#*

```C#
IEnumerable<int> doGenerator()
{
    // внутри любая сложная логика
    for(var i = 0; i < 100; i++)
        yield return i;
}

// Как использовать
foreach(var i in doGenerator())
    console.WriteLine(i)
```

### *Вот как это делает TS*

```TS
function *doGenerator(): Generator<number>
{
    // внутри любая сложная логика
    for(var i = 0; i < 100; i++)
        yield 1;
}


// Как использовать
for(const i of doGenerator())
    console.log(i);
```

### *Вот как это делает Python*

```python
def do_generator():
    for i in range(0, 100):
        yield i

# Как использовать
for value in do_generator():
    print(value)

```

### yield есть в Kotlin, C#, F#, JS, TypeScript, PHP, Python, обещали сделать в Rust

## Нет Индексаторов

### *Вот как это делает C#*

```C#
public class SampleCollection<T>
{
    private T[] arr = new T[100]; // Внутренний массив для хранения данных

    // Индексатор позволяет клиентскому коду использовать синтаксис myCollection[index]
    public T this[int i]
    {
        get => arr[i];         // Возвращает элемент по индексу
        set => arr[i] = value; // Устанавливает значение по индексу
    }
}
```

### Индексаторы есть в Kotlin, C#, F#, Rust
*TypeScript (Не совсем то что требуется)*


## Нет удобного синтаксиса создания объекта и инициализации свойств

```Java
var obj = new MyObjet();
obj.Name = "Alice";
obj.Age = 30;
```

### *Вот как это делает C#*

```C#
var obj = new MyObjet{ Name = "Alice", Age = 30 };
```

### *Вот как это делает Golang*

```Golang
obj := MyObjet{ Name: "Alice", Age: 30 }
```

### *Вот как это делает Rust*

```Rust
let obj = MyObjet{ Name: "Alice", Age: 30 }
```

## Нет нормальных замыканий

*Так не работает, нужно делать объект, вот его свойство можно менять*

```Java
// Объявлена функция
private void f(Runnable p) {
    for(var i = 0; i < 10; i++) {
        p.run();
    }
}

var count = 0;
f(() -> { count++; });
System.out.println(count);
```

### *Вот как это делает C#*

```C#
// Объявлена функция
void f(Action a)
{
    for(var i = 0; i < 10; i++)
        a();
}

var count = 0;
f(() => { count++; });
Console.WriteLine(count);
```

### *Вот как это делает TS*

```TS
// Объявлена функция
f(a: ()=>void): void
{
    for(var i = 0; i < 10; i++)
        a();
}

let count = 0;
f(() => { count++; });
Console.log(count);
```

### *Вот как это делает Python*

```Python
# Объявлена функция
def f(func: Callable[..., None]) -> None:
    for _ in range(0, 10):
        func()


# Как использовать
count = 0

def a() -> None:
    nonlocal count  # Указываем, что нужно работать с count из внешней функции
    count += 1

f(a)
print(count)
```

## Нет перезагрузки операторов

### Перезагрузка есть в Kotlin, C#, F#, Rust

*В Golang тоже нет*
