Fix-Sashok  
==========
Модифицированная и улучшенная версия лаунчера для игры Minecraft от Sashok724  
### Основные отличия 
- Поддержка Minecraft старых версий (до 1.5.2) и новых (от 1.6)
- Наличие функции регистрации в лаунчере (только для сайтов с шифрованием md5 и CMS DLE)
- Наличие связанной с плагином UltraBans функции бана. 
   
### Содержимое WEB-части
* libraries.jar -> Библиотеки клиента.
* Forge.jar -> Minecraft Forge.
* extra.jar -> Запасной джарник для OptiFine, PlayerApi, GuiApi и т.д.
* client.zip -> Содержит bin/natives для lwjgl, config для модов.  
Добавлен assets.zip Звуки для клиентов 1.6.+, должен быть одинаковым размером во всех клиентах на сайте версии 1.6.+)
* Список серверов теперь редактируется на сайте servers.php.
* Дописаны скрипты для авторизации 1.7.2-1.7.4.
Ссылки на новые скрипты указываем в классе YggdrasilMinecraftSessionService.class
"http://minecraft/site/"
"http://minecraft/site/j.php"
"http://minecraft/site/h.php"



Запуск новых версий теперь в аплете лаунчера.
Полное шифрование запросов лаунчер-вебчасть.

Полное изменение загрузки клиента.

Новая структура клиента должна быть такой
clients/assets/ ресурс файлы. При режиме zip clients/assets.zip
clients/voxelaria/config.zip конфиги модов и ресурскаки, расспаковывается в корень папки клиента.
clients/voxelaria/bin/ jar файлы клиента + папка natives, можно
использовать подпапки bin/libraries/ и тд.
clients/voxelaria/mods/  zip-jar файлы, модов, можно использовать
подпапки mods/lib/lib.jar
clients/voxelaria/coremods/ zip-jar файлы коремодов (используется
только устаревшими версиями minecraft)

Приватный сервер terafirmacraft 1.6.4.                                                                                 
Кто хочет повыживать с нами в самом суровом моде terafirmacraft, го к нам.  
Авторизация с моджанг (лицензия).  
Готовый клиент -> http://yadi.sk/d/-xkbOm13QJDQX  
ip 144.76.234.195:20065   
