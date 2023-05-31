 Текстовое описание для диаграммы классов
Registration: 
Отвечает за добавление пользователя в базу данных программы. (иными словами, отвечает за регистрацию пользователя в приложении).
Private:
•	Name:String
•	Phone_Number:String
•	Email_Addres:String
Public:
•	setNumber()
•	setName()
•	setEmail()
•	add_New_User()

Autorization:
Класс авторизации пользователя, выполняется после регистрации пользователя в базе.
Private:
•	Login:String
•	Password:String
•	isPhon_or_Email:String

Public:
•	get_Login()
•	get_Password()
•	Autorization()
•	Get_user()


User: Класс отвечающий за показ информации о пользователе: его регистрационные данные, избранные места, история посещений, текущее выбранное место для посещения. 
Private:
•	userinfo
•	list_of_favorits
•	history
•	currentTravel
Public:
•	Admin: Bool
•	changeHistory()
•	getUserInfo()
•	changeUserInfo()
•	showCurrentTravel()
•	getHistory()

Map:
Класс на котором располагается вся информация о достопримечательностях, заключенная в метках.
Private:
•	mark
•	Mymark
•	Navigation
Public:
•	getInfoAboutMark()
•	getNavigate()
•	changeMark()

Mark:
Класс хранящий информацию о самой метке.
Private:
•	mark_type
•	place
•	Navigation
Public:
•	setMarkType()
•	getMarkType()
•	addMark()
•	getlist_of_Mark()

Place:
Класс хранящий информацию о достопримечательности и о ее месте расположения.
Private:
•	currentplace
Public:
•	 getplace()
•	setplace()
•	addlist_of_Place()
•	get_info()
•	set_info()
•	change_info()

Favourites:
Класс отвечающий за добавление в избранное.
Private:
•	placename
•	placelist
Public:
•	setplace()
•	getPlace()
•	addPlace()
•	removeplace()
•	getinfo()

