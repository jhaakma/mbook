var db = connect('127.0.0.1:27017/mbook');
var all = null;
db.page.drop()
//Menu pages
db.page.insert({'title':'Videos', 'href':'/videos', 'showInMenu':true, 'allowedRoles':[]});
db.page.insert({'title':'User List', 'href':'/userlist', 'showInMenu':true, 'allowedRoles':['USER', 'ADMIN'] });
db.page.insert({'title':'Dashboard', 'href':'/dashboard', 'showInMenu':true, 'allowedRoles':['ADMIN'] });
db.page.insert({'title':'Profile', 'href':'/profile', 'showInMenu':true, 'allowedRoles':['USER', 'ADMIN'] });
//Hidden pages
db.page.insert({'title':'Sign Up', 'href':'/signup', 'showInMenu':false, 'allowedRoles':[]});
db.page.insert({'title':'Log In', 'href':'/login', 'showInMenu':false, 'allowedRoles':[]});
db.page.insert({'title':'Confirm Registration', 'href':'/confirmRegistration', 'showInMenu':false, 'allowedRoles':[]});

all = db.page.find();
while (all.hasNext()) {
    printjson(all.next());
}