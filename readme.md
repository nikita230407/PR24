# Общая идея проекта

Проект представляет собой Android-приложение на Jetpack Compose для кофейни. В приложении есть каталог товаров, корзина, карта кофеен и экран подробной информации о товаре.

Изначально приложение было ближе к схеме из методички:

```text
CatalogScreen -> ProductScreen -> BasketScreen
```

То есть пользователь сначала попадал в каталог, затем переходил на экран товара, а уже после этого попадал в корзину или к оформлению заказа.

После анализа стало понятно, что такая схема плохо подходит для текущего приложения. В проекте уже реализована полноценная корзина: товары можно добавлять, увеличивать количество, уменьшать количество, считать итоговую стоимость и оформлять заказ. Поэтому логика, где ProductScreen является обязательным промежуточным этапом заказа, стала неудобной.

В результате была выбрана более естественная схема для приложения-каталога:

```text
CatalogScreen
    BasketScreen
    MapScreen
    ProductScreen
```

Теперь CatalogScreen является главным экраном. Из него пользователь может:

1. Открыть корзину.
2. Открыть карту кофеен.
3. Открыть подробную карточку товара.
4. Добавить товар в корзину прямо из каталога.

Такой вариант лучше соответствует реальному поведению интернет-каталога или приложения кофейни. Пользователь не обязан каждый раз проходить через экран товара, чтобы попасть в корзину. При этом экран товара остается полезным: он показывает описание, цену и дополнительные параметры товара.

# Почему было решено отойти от схемы из методички

Методичка показывает общий пример тестирования и переходов между экранами. Там важна сама идея: создать несколько экранов, сделать переходы и написать UI/UX тесты. Но конкретная структура экранов должна соответствовать логике приложения.

В нашем проекте появилась рабочая корзина. Это изменило архитектуру приложения.

Если оставить схему:

```text
CatalogScreen -> ProductScreen -> BasketScreen
```

то возникают проблемы:

1. ProductScreen становится обязательным этапом заказа, хотя по смыслу это должна быть карточка товара.
2. Нельзя быстро открыть корзину из каталога.
3. Кнопка карты кофеен внутри конкретного товара выглядит лишней, потому что карта относится ко всей кофейне, а не к одному товару.
4. Пользовательский путь становится длиннее: для простого заказа нужно делать больше переходов.
5. Корзина уже умеет хранить состояние, поэтому ее логичнее открыть напрямую из каталога.

Поэтому была выбрана другая схема:

```text
CatalogScreen -> BasketScreen
CatalogScreen -> MapScreen
CatalogScreen -> ProductScreen
ProductScreen -> BasketScreen
```

Это сохраняет требования практической работы: есть несколько экранов, навигация, интерактивные элементы, заполнение полей, сохранение данных и тесты. Но при этом приложение стало логичнее для пользователя.

# Текущая структура проекта

Основной код приложения находится в папке:

```text
app/src/main/java/com/example/PR24-pr-23.103-fn
```

Важные части проекта:

```text
com.example.PR24-pr-23.103-fn
    MainActivity.kt
    navigation
        AppNavGraph.kt
        Routes.kt
    ui
        model
            Product.kt
            CartManager.kt
            UserData.kt
        screens
            CatalogScreen.kt
            ProductScreen.kt
            BasketScreen.kt
            MapScreen.kt
        theme
            Color.kt
            Theme.kt
            Type.kt
```

Тесты находятся в папке:

```text
app/src/androidTest/java/com/example/PR24-pr-23.103-fn
```

Там есть две группы тестов:

```text
ui
    BasketScreenUiTest.kt
    CatalogScreenUiTest.kt
    MapScreenUiTest.kt
    ProductScreenUiTest.kt
ux
    InteractionTest.kt
    NavigationTest.kt
    SaveDataTest.kt
```

# MainActivity

Файл:

```text
app/src/main/java/com/example/PR24-pr-23.103-fn/MainActivity.kt
```

MainActivity является точкой входа в приложение. В ней вызывается:

```kotlin
enableEdgeToEdge()
```

Это включает отображение интерфейса от края до края экрана. Из-за этого у экранов используются безопасные отступы:

```kotlin
.safeDrawingPadding()
```

Если не добавлять safeDrawingPadding, элементы интерфейса могут заходить под системную строку состояния, навигационную панель или жестовую область Android.

Также MainActivity вызывает:

```kotlin
setContent {
    AppNavGraph()
}
```

Это значит, что весь интерфейс приложения строится через граф навигации AppNavGraph.

# Навигация

Навигация находится в файлах:

```text
app/src/main/java/com/example/PR24-pr-23.103-fn/navigation/Routes.kt
app/src/main/java/com/example/PR24-pr-23.103-fn/navigation/AppNavGraph.kt
```

В Routes.kt хранятся названия маршрутов:

```kotlin
object Routes {
    const val CATALOG = "catalog"
    const val PRODUCT = "product"
    const val MAP = "map"
    const val BASKET = "basket"

    fun productRoute(productId: Int): String {
        return "$PRODUCT/$productId"
    }
}
```

Маршрут product сделан с параметром productId. Это нужно для того, чтобы открыть карточку конкретного товара.

Например:

```kotlin
Routes.productRoute(1)
```

вернет маршрут:

```text
product/1
```

В AppNavGraph создается navController:

```kotlin
val navController = rememberNavController()
```

Он отвечает за переходы между экранами.

Главный экран:

```kotlin
startDestination = Routes.CATALOG
```

Это значит, что приложение всегда открывается с каталога.

# Почему CatalogScreen стал главным экраном

CatalogScreen стал главным экраном, потому что он показывает список товаров и является естественной начальной точкой приложения.

Из каталога можно перейти:

1. В корзину.
2. На карту кофеен.
3. В подробную карточку товара.

В AppNavGraph это реализовано так:

```kotlin
CatalogScreen(
    onBasketClick = {
        navController.navigate(Routes.BASKET)
    },
    onMapClick = {
        navController.navigate(Routes.MAP)
    },
    onProductClick = { productId ->
        navController.navigate(Routes.productRoute(productId))
    }
)
```

CatalogScreen не знает напрямую о navController. Он просто принимает функции:

```kotlin
onBasketClick
onMapClick
onProductClick
```

Это хороший подход для Compose, потому что экран отвечает только за отображение и события, а навигация остается в AppNavGraph.

# Модель товара

Модель товара находится в файле:

```text
app/src/main/java/com/example/PR24-pr-23.103-fn/ui/model/Product.kt
```

Основной класс:

```kotlin
data class Product(
    val id: Int,
    val title: String,
    val price: Int,
    val image: Int,
    val description: String,
    val hasSugarOption: Boolean = false,
    val hasCinnamonOption: Boolean = false,
    var count: Int = 1
)
```

Поля:

1. id: уникальный идентификатор товара.
2. title: название товара.
3. price: цена товара.
4. image: ссылка на drawable-ресурс с изображением.
5. description: описание товара.
6. hasSugarOption: показывает, нужно ли показывать переключатель сахара.
7. hasCinnamonOption: показывает, нужно ли показывать переключатель корицы.
8. count: количество товара в корзине.

Для хранения товаров используется объект ProductCatalog:

```kotlin
object ProductCatalog {
    val items = listOf(...)

    fun findById(id: Int): Product {
        return items.first { it.id == id }
    }
}
```

Почему товары вынесены в ProductCatalog:

1. Каталог и экран товара используют одни и те же данные.
2. Не нужно дублировать список товаров в разных экранах.
3. По id можно открыть конкретный товар.
4. Логика стала проще для тестирования.

# Корзина

Корзина реализована в файле:

```text
app/src/main/java/com/example/PR24-pr-23.103-fn/ui/model/CartManager.kt
```

CartManager сделан как object:

```kotlin
object CartManager
```

Это значит, что он существует в одном экземпляре на все приложение. Для учебного проекта такой вариант удобен, потому что корзина доступна из каталога, карточки товара и экрана корзины.

Список товаров:

```kotlin
val products = mutableStateListOf<Product>()
```

Используется именно mutableStateListOf, потому что это observable-список Compose. Когда товары добавляются или меняются, интерфейс может автоматически обновиться.

Добавление товара:

```kotlin
fun addProduct(product: Product) {
    val index =
        products.indexOfFirst { it.id == product.id }

    if (index != -1) {
        val existing = products[index]
        products[index] = existing.copy(
            count = existing.count + 1
        )
    }
    else {
        products.add(
            product.copy(count = 1)
        )
    }
}
```

Логика такая:

1. Проверяем, есть ли товар с таким id в корзине.
2. Если есть, увеличиваем count.
3. Если нет, добавляем новый товар с count = 1.

Важно, что используется copy:

```kotlin
products[index] = existing.copy(count = existing.count + 1)
```

Это лучше, чем просто менять поле count у объекта. При замене элемента в mutableStateListOf Compose надежнее замечает изменение и обновляет интерфейс.

Увеличение количества:

```kotlin
fun increase(product: Product)
```

Уменьшение количества:

```kotlin
fun decrease(product: Product)
```

При уменьшении есть проверка:

```kotlin
if (index != -1 && products[index].count > 1)
```

Количество не опускается ниже 1. Это сделано, чтобы в корзине не появлялись товары с нулевым или отрицательным количеством.

Подсчет количества товаров:

```kotlin
fun itemsCount(): Int {
    return products.sumOf { it.count }
}
```

Эта функция используется в CatalogScreen для текста:

```text
Товаров в корзине: N
```

Подсчет итоговой стоимости:

```kotlin
fun totalPrice(): Int {
    return products.sumOf {
        it.price * it.count
    }
}
```

Итоговая стоимость используется на экране корзины и в сообщении о заказе.

# CatalogScreen

Файл:

```text
app/src/main/java/com/example/PR24-pr-23.103-fn/ui/screens/CatalogScreen.kt
```

CatalogScreen показывает:

1. Название кофейни.
2. Количество товаров в корзине.
3. Сетку товаров.
4. Кнопку открытия корзины.
5. Кнопку открытия карты кофеен.

Список товаров берется из ProductCatalog:

```kotlin
val items = ProductCatalog.items
```

Количество товаров в корзине:

```kotlin
Text(
    text = "Товаров в корзине: ${CartManager.itemsCount()}",
    modifier = Modifier.testTag("cart_count")
)
```

Сетка товаров сделана через LazyVerticalGrid:

```kotlin
LazyVerticalGrid(
    columns = GridCells.Fixed(2),
    contentPadding = PaddingValues(8.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp),
    horizontalArrangement = Arrangement.spacedBy(16.dp),
    modifier = Modifier.weight(1f)
)
```

Почему используется LazyVerticalGrid:

1. Товары выглядят как каталог.
2. На экране помещается несколько карточек.
3. Это лучше подходит для магазина, чем вертикальный список.
4. LazyVerticalGrid эффективнее для большого списка, потому что Compose отрисовывает только видимые элементы.

Каждая карточка товара кликабельна:

```kotlin
Card(
    modifier = Modifier
        .clickable {
            onProductClick(item.id)
        }
        .testTag("product_card_${item.id}")
)
```

При нажатии открывается ProductScreen для конкретного товара.

Также внутри карточки есть кнопка:

```kotlin
Button(
    onClick = {
        CartManager.addProduct(item)
    },
    modifier = Modifier
        .fillMaxWidth()
        .testTag("add_to_cart_${item.id}")
)
```

Эта кнопка позволяет добавить товар в корзину без открытия подробного экрана.

Почему это удобно:

1. Если пользователь уже знает товар, он добавляет его сразу.
2. Если пользователь хочет подробности, он нажимает на карточку.
3. Каталог становится быстрее и удобнее.

# ProductScreen

Файл:

```text
app/src/main/java/com/example/PR24-pr-23.103-fn/ui/screens/ProductScreen.kt
```

ProductScreen теперь является экраном подробной информации о товаре.

Он принимает:

```kotlin
fun ProductScreen(
    product: Product,
    onAddToCartClick: () -> Unit,
    onBackClick: () -> Unit
)
```

Параметры:

1. product: товар, который нужно показать.
2. onAddToCartClick: действие после добавления товара в корзину.
3. onBackClick: действие для кнопки назад.

На экране показывается:

1. Кнопка Назад.
2. Название товара.
3. Изображение товара.
4. Описание товара.
5. Цена.
6. Переключатель сахара, если товар поддерживает эту опцию.
7. Переключатель корицы, если товар поддерживает эту опцию.
8. Кнопка Добавить в корзину.

Кнопка назад:

```kotlin
Button(
    onClick = onBackClick,
    modifier = Modifier.testTag("product_back_button")
)
```

Она вызывает:

```kotlin
navController.popBackStack()
```

Это возвращает пользователя на предыдущий экран.

Кнопка карты кофеен была убрана из ProductScreen. Это сделано потому, что карта относится не к конкретному товару, а ко всей кофейне. Теперь карта открывается из CatalogScreen, где она логически уместнее.

Добавление товара в корзину:

```kotlin
Button(
    onClick = {
        CartManager.addProduct(product)
        onAddToCartClick()
    },
    modifier = Modifier
        .fillMaxWidth()
        .testTag("product_add_button")
)
```

После добавления товара пользователь переходит в корзину.

Длинный экран поддерживает прокрутку:

```kotlin
.verticalScroll(rememberScrollState())
```

Это важно, потому что на маленьких экранах все элементы могут не поместиться сразу.

# BasketScreen

Файл:

```text
app/src/main/java/com/example/PR24-pr-23.103-fn/ui/screens/BasketScreen.kt
```

BasketScreen показывает:

1. Кнопку Назад.
2. Название экрана Корзина.
3. Список товаров в корзине.
4. Кнопки уменьшения и увеличения количества.
5. Итоговую стоимость.
6. Поле имени.
7. Поле возраста.
8. Кнопку Купить.
9. Сообщение о принятом заказе.

Кнопка назад:

```kotlin
Button(
    onClick = onBackClick,
    modifier = Modifier.testTag("basket_back_button")
)
```

Она возвращает пользователя на предыдущий экран.

Список товаров берется из:

```kotlin
CartManager.products
```

Для каждого товара показывается изображение, название, цена и количество.

Уменьшение количества:

```kotlin
CartManager.decrease(product)
```

Увеличение количества:

```kotlin
CartManager.increase(product)
```

Итоговая стоимость:

```kotlin
Text(
    text = "Итого: ${CartManager.totalPrice()} ₽",
    modifier = Modifier.testTag("basket_total")
)
```

Поля ввода:

```kotlin
OutlinedTextField(
    value = name,
    onValueChange = {
        name = it
    }
)
```

```kotlin
OutlinedTextField(
    value = age,
    onValueChange = {
        age = it
    }
)
```

Для хранения введенных данных используется remember:

```kotlin
var name by remember {
    mutableStateOf("")
}
```

```kotlin
var age by remember {
    mutableStateOf("")
}
```

Сообщение о заказе:

```kotlin
var orderMessage by remember {
    mutableStateOf("")
}
```

При нажатии на кнопку Купить формируется строка:

```kotlin
orderMessage =
    "Заказ принят: $name, $age, ${CartManager.totalPrice()} Руб"
```

Также вызывается snackbar:

```kotlin
snackbarHostState.showSnackbar(orderMessage)
```

То есть пользователь получает оповещение в формате:

```text
Заказ принят: имя, возраст, стоимость Руб
```

Например:

```text
Заказ принят: Иван, 18, 500 Руб
```

Дополнительно сообщение выводится постоянным текстом:

```kotlin
if (orderMessage.isNotEmpty()) {
    Text(
        text = orderMessage,
        modifier = Modifier.testTag("order_status")
    )
}
```

Это сделано по двум причинам:

1. Пользователь видит результат заказа даже после исчезновения snackbar.
2. UI-тест может стабильно проверить, что заказ принят.

# MapScreen

Файл:

```text
app/src/main/java/com/example/PR24-pr-23.103-fn/ui/screens/MapScreen.kt
```

MapScreen показывает изображение карты и кнопку назад.

Карта открывается из CatalogScreen, а не из ProductScreen.

Почему так лучше:

1. Карта относится ко всей кофейне.
2. Она не зависит от выбранного товара.
3. Пользователь может открыть карту сразу из главного экрана.
4. Карточка товара не перегружается лишней кнопкой.

Кнопка назад:

```kotlin
FloatingActionButton(
    onClick = onBackClick,
    modifier = Modifier
        .align(Alignment.BottomEnd)
        .padding(16.dp)
        .testTag("map_back_button")
)
```

# Edge-to-edge и отступы

В MainActivity используется:

```kotlin
enableEdgeToEdge()
```

Поэтому экраны используют:

```kotlin
.safeDrawingPadding()
```

Это есть в:

1. CatalogScreen.
2. ProductScreen.
3. BasketScreen.
4. MapScreen.

Для длинных экранов добавлена прокрутка:

```kotlin
.verticalScroll(rememberScrollState())
```

Она используется в:

1. ProductScreen.
2. BasketScreen.

CatalogScreen использует LazyVerticalGrid с весом:

```kotlin
modifier = Modifier.weight(1f)
```

Поэтому сетка занимает доступное место между верхней частью экрана и нижними кнопками.

# testTag и тестируемость

В проекте активно используются testTag:

```kotlin
Modifier.testTag("catalog_button")
```

testTag нужен для UI-тестов. Он позволяет найти конкретный элемент интерфейса независимо от текста.

Это особенно важно, потому что текст может поменяться, а testTag остается стабильным.

Примеры тегов:

```text
catalog_title
cart_count
catalog_button
catalog_map_button
product_card_1
product_add_button
product_back_button
basket_back_button
basket_total
buy_button
order_status
map_back_button
```

Благодаря этим тегам тесты могут проверять:

1. Отображение экранов.
2. Переходы между экранами.
3. Нажатия на кнопки.
4. Ввод имени и возраста.
5. Отображение сообщения о заказе.

# UI-тесты

UI-тесты находятся в:

```text
app/src/androidTest/java/com/example/pr24/ui
```

Они проверяют, что основные элементы экранов отображаются.

CatalogScreenUiTest проверяет:

1. Название каталога.
2. Кнопку корзины.
3. Кнопку карты.
4. Счетчик товаров в корзине.

ProductScreenUiTest проверяет:

1. Открытие карточки товара.
2. Название товара.
3. Описание товара.
4. Кнопку Добавить в корзину.
5. Кнопку Назад.

MapScreenUiTest проверяет:

1. Переход на карту из каталога.
2. Наличие кнопки Назад на карте.

BasketScreenUiTest проверяет:

1. Открытие корзины из каталога.
2. Название экрана.
3. Итоговую стоимость.
4. Кнопку Назад.
5. Кнопку Купить.

# UX-тесты

UX-тесты находятся в:

```text
app/src/androidTest/java/com/example/pr24/ux
```

Они проверяют не просто наличие элементов, а пользовательские сценарии.

NavigationTest проверяет переходы:

1. Каталог -> карта.
2. Карта -> назад.
3. Каталог -> карточка товара.
4. Карточка товара -> назад.
5. Карточка товара -> добавить в корзину.
6. Переход в корзину.

InteractionTest проверяет интерактивные элементы:

1. Открытие карточки товара.
2. Переключатель сахара.
3. Переключатель корицы.
4. Добавление товара в корзину.
5. Отображение корзины.

SaveDataTest проверяет оформление заказа:

1. Открытие корзины.
2. Ввод возраста.
3. Ввод имени.
4. Нажатие Купить.
5. Появление order_status с введенными данными.

# Почему тесты используют createComposeRule

В тестах используется:

```kotlin
createComposeRule()
```

И далее:

```kotlin
composeRule.setContent {
    AppNavGraph()
}
```

Так тесты запускают сам Compose-интерфейс напрямую.

Это удобно для учебного проекта:

1. Не нужно зависеть от полного запуска MainActivity.
2. Тестируется реальный AppNavGraph.
3. Проверяются настоящие переходы между экранами.
4. Тесты становятся проще и стабильнее.

# Что было сделано в итоге

В проекте была изменена структура экранов и логика пользовательского пути.

Сделано:

1. CatalogScreen стал главным экраном приложения.
2. В каталоге добавлен счетчик товаров в корзине.
3. В каталоге добавлена кнопка открытия корзины.
4. В каталоге добавлена кнопка открытия карты кофеен.
5. Карточки товаров стали кликабельными.
6. По нажатию на карточку открывается ProductScreen конкретного товара.
7. ProductScreen стал экраном подробной информации о товаре.
8. Из ProductScreen убрана кнопка карты кофеен.
9. В ProductScreen добавлена кнопка Назад.
10. В ProductScreen оставлена кнопка Добавить в корзину.
11. BasketScreen получил кнопку Назад.
12. Корзина отображает товары, количество и итоговую стоимость.
13. В корзине можно менять количество товаров.
14. В корзине можно ввести имя и возраст.
15. После покупки появляется сообщение Заказ принят: имя, возраст, стоимость Руб.
16. Сообщение показывается через snackbar и постоянным текстом order_status.
17. Навигация переведена на схему с productId.
18. Тесты обновлены под новую структуру.
19. Все UI/UX тесты проходят.

# Итоговая схема приложения

```text
CatalogScreen
    Открыть корзину -> BasketScreen
    Карта кофеен -> MapScreen
    Нажатие на товар -> ProductScreen
    Добавить товар -> CartManager

ProductScreen
    Назад -> предыдущий экран
    Добавить в корзину -> BasketScreen

BasketScreen
    Назад -> предыдущий экран
    Купить -> сообщение о заказе

MapScreen
    Назад -> предыдущий экран
```

# Проверка работы

Проект был проверен командой:

```text
.\gradlew.bat :app:connectedDebugAndroidTest
```

Результат:

```text
BUILD SUCCESSFUL
7 tests, 0 failures
```

Это значит, что приложение собирается, инструментальные UI/UX тесты запускаются, основные пользовательские сценарии работают.

# Вывод

В результате приложение стало ближе к реальному мобильному приложению кофейни. Вместо линейного сценария из методички была сделана структура с главным каталогом, рабочей корзиной, подробным экраном товара и отдельной картой кофеен.

Такой вариант лучше, потому что он учитывает уже реализованную корзину. Пользователь может быстро добавлять товары, смотреть подробности только при необходимости, открывать корзину напрямую и оформлять заказ с итоговой стоимостью.

Методичка использовалась как основа для требований по экранам, тестам и проверке UX. Но конкретная архитектура была адаптирована под фактическую логику проекта, чтобы приложение выглядело цельным и работало удобнее.
# PR24
