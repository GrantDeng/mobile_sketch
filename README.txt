

MobileSketch:
I develop this android app in OS X. The main development tool I use is Intellij IDEA and Android Studio.

The application is a Android simple drawing tool, I design by using MVC pattern. There is a “DataModel.java” file to store the data of the application.  And there is a UI activity called CanvasActicity which handle the ui, including different display mode. The things that draw in the board is using a view called CanvasView.

MobileSketch’s function is shown in the toolbar, there is a selectbutton can select the uppest shape user draw in canvas, the shape boarder will become red if the user select it. After selecting it user can also drag to move the shape. Choose other button will cancel the selection. The erase button will delete the uppest shape in the position of user’s touch. There is three shape button that user can choose to draw. The fill button can fill the shape user draw with current selected color. There is also a button call ‘c’ in the most right or button to clean the entire canvas.
The action user choose, current color, and also current stroke will shown in the toolbar.

The application support both portrait and landscape mode. 

I have try to do the save function. in the button A on portrait mode. for now it will create a img.jpeg file.
