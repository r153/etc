Editor.GoLineTop(0x08);
var str=Editor.GetLineStr(0);
if (str.indexOf('//')==0){
	Editor.Right();
	Editor.Right();
	Editor.LineDeleteToStart();
}else{
	Editor.InsText('//');
}
Editor.GoLineTop(0x08);
Editor.Down();
