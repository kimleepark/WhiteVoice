package com.example.myapplicationui.Function;

import android.app.Activity;
import android.util.Log;
import android.widget.TextView;

import com.example.myapplicationui.Conection.pathListItem;
import com.example.myapplicationui.User_Interface.MenuActivity;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.Source;

import java.net.URL;
import java.util.ArrayList;

/**
 * Created by 박지찬 on 2017-08-03.
 */
public class ParsingClass extends Activity{

    DebugClass Debugs = new DebugClass();

    public ArrayList<pathListItem> pathListItems = new ArrayList<pathListItem>();

    public static final String KEY_SIMPLE_DATA = "data";

    public double StartX;
    public double StartY;
    public double [] placex= new double[100];
    public double [] placey= new double [100];
    public int i;

    public String statingmap;       //시작하는곳의 이름(문자)
    public String destinationmap;
    public String startingx;        //파싱으로 가져온 시작지 좌표
    public String startingy;
    public String destinationx;     //파싱으로 가져온 도착지 좌표
    public String destinationy;

    public String encryx;           //파싱으로 가져온 암호화된 도착 좌표
    public String encryy;
    public String encryx1;           //파싱으로 가져온 암호화된 시작 좌표
    public String encryy1;
    public String mentCopy[] = new String[100];
    

    private static Thread thread = null;
    String parsing_url;  // 파싱해오고자 하는 URL
    String get_data;  // 파싱해서 가져온 데이터를 저장할 스트링 변수
    ArrayList<String> array;  // get_data 변수의 값을 순차적으로 저장할 배열
    TextView textView1;  // 제목을 표시해줄 텍스트뷰

    public void onLoad(){
        //여기부터 추가한코드
        parsing_url = "https://apis.daum.net/local/geo/transcoord?apikey=1b9fd99de0b3b55cd8bfca913c787474&fromCoord=WGS84&y="+StartX+"&x="+StartY+"&toCoord=wtm&output=json";
        Runnable task = new Runnable(){
            public void run(){
                getTmLocation(parsing_url);  //getData2는 출발지와 도착지의 각 x,y값을 찾는 메소드임
            }
        };
        thread = new Thread(task);
        thread.start();  // 반드시 쓰레드를 해줘야함 그 이유는 아래에서 설명
        try{
            thread.join();  // 쓰레드 작업 끝날때까지 다른 작업들은 대기
        }catch(Exception e){
        }
        startingx = destinationx;
        startingy = destinationy;


        parsing_url = "https://m.map.daum.net/actions/routeView?ex="+encryx+"&ey="+encryy+"&endLoc=ㅁㅁㅁ&ids=%2CP000";
        task = new Runnable(){
            public void run(){
                getData3(parsing_url);
            }
        };
        thread = new Thread(task);
        thread.start();  // 반드시 쓰레드를 해줘야함 그 이유는 아래에서 설명
        try{
            thread.join();  // 쓰레드 작업 끝날때까지 다른 작업들은 대기
        }catch(Exception e){
        }
        //도착지설정
        parsing_url = "https://m.map.daum.net/actions/searchView?q="+destinationmap+"&wxEnc="+encryx+"&wyEnc="+encryy+"&lvl=4&rcode=B7218A141&sort=2&viewmap=false";
        Debugs.logv(new Exception(),"something to print "+ parsing_url);
        task = new Runnable(){
            public void run(){
                getData2(parsing_url);
            }
        };
        thread = new Thread(task);
        thread.start();  // 반드시 쓰레드를 해줘야함 그 이유는 아래에서 설명
        try{
            thread.join();  // 쓰레드 작업 끝날때까지 다른 작업들은 대기
        }catch(Exception e){
        }

        //여기까지 추가한 코드

        //새로 추가한 코드
        encryx1 = encryx;
        encryy1 = encryy;

        parsing_url = "https://m.map.daum.net/actions/routeView?ex="+destinationx+"&ey="+destinationy+"&endLoc=ㅁㅁㅁ&ids=%2CP000";
        Debugs.logv(new Exception(),"something to print "+ parsing_url);
        task = new Runnable(){
            public void run(){
                getData3(parsing_url);
            }
        };
        thread = new Thread(task);
        thread.start();  // 반드시 쓰레드를 해줘야함 그 이유는 아래에서 설명
        try{
            thread.join();  // 쓰레드 작업 끝날때까지 다른 작업들은 대기
        }catch(Exception e){
        }

        //여기까지 새로 추가한 코드



        //여기 textView1 = (TextView)findViewById(R.id.textView1);
        //parsing_url = "http://map.daum.net/route/walkset.json?callback=jQuery1810369897711_1499578448313&sName=%EA%B2%BD%EA%B8%B0+%EB%B6%80%EC%B2%9C%EC%8B%9C+%EC%A4%91%EB%8F%99+982&sX=449528&sY=1108343&eName=%EA%B2%BD%EA%B8%B0+%EB%B6%80%EC%B2%9C%EC%8B%9C+%EC%8B%AC%EA%B3%A1%EB%8F%99+364-1&eX=449288&eY=1109118&ids=%2C";
        parsing_url = "https://m.map.daum.net/actions/walkRoute?startLoc="+"현위치"+"&sxEnc="+ encryx1 +"&syEnc="+encryy1+"&endLoc="+"목적지"+"&exEnc="+encryx+"&eyEnc="+encryy+"&ids=%2CP000&service=";
        Debugs.logv(new Exception(),"something to print "+ parsing_url);
        task = new Runnable(){
            public void run(){
                //getData(parsing_url);
                Debugs.logv(new Exception(), parsing_url);
                pathListItems = recallPathListItem(getData(parsing_url));
            }
        };
        thread = new Thread(task);
        thread.start();  // 반드시 쓰레드를 해줘야함 그 이유는 아래에서 설명
        try{
            thread.join();  // 쓰레드 작업 끝날때까지 다른 작업들은 대기
        }catch(Exception e){
        }

        for(i=0;i<pathListItems.size();i++) {
            parsing_url = "https://apis.daum.net/local/geo/transcoord?apikey=1b9fd99de0b3b55cd8bfca913c787474&fromCoord=wtm&y=" + placey[i] * 0.4 + "&x=" + placex[i] * 0.4 + "&toCoord=WGS84&output=json";
            task = new Runnable() {
                public void run() {
                    getWGS84Location(parsing_url);  //getData2는 출발지와 도착지의 각 x,y값을 찾는 메소드임
                }
            };
            thread = new Thread(task);
            thread.start();  // 반드시 쓰레드를 해줘야함 그 이유는 아래에서 설명
            try {
                thread.join();  // 쓰레드 작업 끝날때까지 다른 작업들은 대기
            } catch (Exception e) {
            }
        }
    }

    public String getData2(String strURL){
        Source source;
        get_data = "";
        try{
            URL url = new URL(strURL);
            source = new Source(url);  // 쓰레드를 사용 안하면 여기에서 예외 발생함 그 이유는 아래에서 설명
            Element element = null;
//            List<Element> list = source.getAllElements(HTMLElementName.SCRIPT); // title 태그의 엘리먼트 가져옴
            //   array.add(source.toString());
            element = source.getAllElements(HTMLElementName.SCRIPT).get(1);

            destinationmap = element.toString();
            destinationmap = destinationmap.substring(destinationmap.indexOf("name : ")+8, destinationmap.length());
            destinationmap = destinationmap.substring(0,destinationmap.indexOf(",")-1);

            statingmap = source.toString();
            statingmap = statingmap.substring(statingmap.indexOf(">위치</span>")+13, statingmap.length());
            statingmap = statingmap.substring(0,statingmap.indexOf("<"));

            destinationx = element.toString();
            destinationx=destinationx.substring(destinationx.indexOf("point_wx : "),destinationx.length());
            destinationx=destinationx.substring(destinationx.indexOf("\'"),destinationx.indexOf(","));
            destinationx=destinationx.substring(1,destinationx.length()-1);

            destinationy = element.toString();
            destinationy=destinationy.substring(destinationy.indexOf("point_wy : "),destinationy.length());
            destinationy=destinationy.substring(destinationy.indexOf("\'"),destinationy.indexOf(","));
            destinationy=destinationy.substring(1,destinationy.length()-1);


        }catch(Exception e){
            destinationmap="에러";
            Debugs.logv(new Exception(),"something to print 에러났셈");
            destinationy="975826";
            destinationx="558705";
            statingmap="안성시 안성2동";
            e.getStackTrace();
            e.getMessage();
        }
        return destinationx;  // 입력된 배열값을 리턴
    }

    public String getData3(String strURL){
        Source source;
        get_data = "";
        try{
            URL url = new URL(strURL);
            source = new Source(url);  // 쓰레드를 사용 안하면 여기에서 예외 발생함 그 이유는 아래에서 설명
            Element element = null;
//            List<Element> list = source.getAllElements(HTMLElementName.SCRIPT); // title 태그의 엘리먼트 가져옴
            //   array.add(source.toString());
            element = source.getAllElements().get(1);

            encryx = element.toString();
            encryx=encryx.substring(encryx.indexOf("exEnc")+28,encryx.length());
            encryx=encryx.substring(0,encryx.indexOf("input")-11);

            encryy = element.toString();
            encryy=encryy.substring(encryy.indexOf("eyEnc")+28,encryy.length());
            encryy=encryy.substring(0,encryy.indexOf("input")-14);



            int a=1;

        }catch(Exception e){
            destinationmap="에러";
            Debugs.logv(new Exception(),"something to print 에러났셈");
            destinationy="975826";
            destinationx="558705";
            statingmap="안성시 안성2동";
            e.getStackTrace();
            e.getMessage();
        }
        return destinationx;  // 입력된 배열값을 리턴
    }

    public ArrayList<String> getData(String strURL){
        int asdf;
        get_data = "";
        array = new ArrayList();
        try{
            int asdfd;
            URL url = new URL(strURL);
            Source source = new Source(url);  // 쓰레드를 사용 안하면 여기에서 예외 발생함 그 이유는 아래에서 설명
            Element element = null;
            asdfd = 1;
            asdfd = asdfd + 5;
//            List<Element> list = source.getAllElements(HTMLElementName.SCRIPT); // title 태그의 엘리먼트 가져옴
            //   array.add(source.toString());
//엘리먼트로 파싱안됨            element = source.getAllElements(HTMLElementName.SCRIPT).get(1);

            String ment = source.toString();
            String pointx = source.toString();
            String pointy = source.toString();
            String points = source.toString();

            int sw=1;
            String sourcedata;
            sourcedata=ment;

            while(sw==1){
                array.add("\n");
                if(ment.indexOf("ment")!=-1){
                    ment=ment.substring(ment.indexOf("ment"),ment.length());
                    pointx=ment;
                    ment=ment.substring(0,ment.indexOf(","));
                    array.add(ment);



                    pointx=pointx.substring(pointx.indexOf("x"),pointx.length());
                    pointy=pointx;
                    pointx=pointx.substring(0,pointx.indexOf(","));
                    array.add(pointx);

                    pointy=pointy.substring(pointy.indexOf("y"),pointy.length());
                    points=pointy;
                    if(pointy.indexOf(",") < pointy.indexOf("}")) {
                        pointy = pointy.substring(0, pointy.indexOf(","));

                        sourcedata = points;

                        points=points.substring(points.indexOf("points"),points.length());
                        points=points.substring(0,points.indexOf(","));
                        array.add(points);
                    }
                    else {
                        pointy = pointy.substring(0, pointy.indexOf("}"));
                        sourcedata = points;
                    }
                    array.add(pointy);
                    ment=sourcedata;
                } else {
                    if(String.valueOf("도보 길찾기 결과가 없습니다.").equals(ment.substring(ment.indexOf("tit_none")+10,ment.indexOf("tit_none")+26)))
                    {
                        destinationmap="에러";
                        Debugs.logv(new Exception(),"something to print 에러났셈2");
                        destinationy="975826";
                        destinationx="558705";
                        statingmap="안성시 안성2동";
                        finish();
                    }
                    sw=0;
                }

            }
            //
        }catch(Exception e){
            destinationmap="에러";
            Debugs.logv(new Exception(),"something to print 에러났셈");
            destinationy="975826";
            destinationx="558705";
            statingmap="안성시 안성2동";
            e.getStackTrace();
            e.getMessage();
        }

        String [] ment= new String [100];

        int mentindex=0;

        for(int i=1;i<array.size();i++){
            if(i==1){
                ment[mentindex]=array.get(i);
                i++;
                ment[mentindex]=ment[mentindex].substring(8,ment[mentindex].length()-1);   //경로 안내에서 필요없는 부분 잘라내기
                placex[mentindex] = Integer.parseInt(array.get(i).substring(5,array.get(i).length()-1));   //x좌표에서 필요없는 부분 잘라내기
                i++;
                placey[mentindex] = Integer.parseInt(array.get(i).substring(5,array.get(i).length()-6));   //y좌표에서 필요없는 부분 잘라내기
                i++;
                mentindex++;
                i++;
            }
            ment[mentindex]=array.get(i);
            ment[mentindex]=ment[mentindex].substring(8,ment[mentindex].length()-1);
            i++;
            placex[mentindex] = Integer.parseInt(array.get(i).substring(5,array.get(i).length()-3));
            i+=2;
            placey[mentindex] = Integer.parseInt(array.get(i).substring(5,array.get(i).length()-3));
            i++;
            if("도착'".equals(array.get(i-4).substring(array.get(i-4).length()-3))) {
                i=array.size();
            }
            mentindex++;
        }

        placex[99]=0;
        //Log.e("LIST 채킹","채크리스트 : " + index +", "+ ment +", "+ pointx +", "+ pointy);

        for(int i=0;i<ment.length;i++){
            mentCopy[i]=ment[i];
        }


        return array;  // 입력된 배열값을 리턴
    }
    public void getWGS84Location(String strURL){
        Source source=null;
        get_data = "";
        try{
            double trans=0;       //좌표값에 0.4를 나눠서 tm값으로 변환하기 위해 넣은 값
            URL url = new URL(strURL);
            source = new Source(url);  // 쓰레드를 사용 안하면 여기에서 예외 발생함 그 이유는 아래에서 설명
            Element element = null;
//            List<Element> list = source.getAllElements(HTMLElementName.SCRIPT); // title 태그의 엘리먼트 가져옴
            //   array.add(source.toString());
            encryx = source.toString();
            encryx = encryx.substring(encryx.indexOf("y")+3,encryx.length()-25);
            trans = Double.parseDouble(encryx);
            placex[i]=trans;

            pathListItems.get(i).setX(trans);

            encryy = source.toString();
            encryy = encryy.substring(encryy.indexOf("x")+3,encryy.length()-2);
            trans = Double.parseDouble(encryy);
            placey[i]=trans;
            pathListItems.get(i).setY(trans);

/*c
         for(int i = 0; i < list.size(); i++){
            element = list.get(i);
            String attributevalue = element.getAttributeValue("type");  // title 태그의 속성값이 type을 찾는다

            if(attributevalue != null){
                if(attributevalue.equalsIgnoreCase("text")){  // type의 값이 text 이면
                    TextExtractor textExtractor = element.getTextExtractor();  // 해당 문자값을 가져온다
                    get_data = textExtractor.toString();  // 가져온 값을 스트링으로 변환후
                    array.add(get_data);  // ArrayList에 추가한다
                }
            }
        }
*/
        }catch(Exception e){
            System.out.print("11");
        }
    }
    public String getTmLocation(String strURL){
        int aaaaaa=0; //첫줄에 뭐 넣으면 작동안해서 쓰레기값 넣음
        Source source=null;
        get_data = "";
        try{
            double trans=0;       //좌표값에 0.4를 나눠서 tm값으로 변환하기 위해 넣은 값
            URL url = new URL(strURL);
            source = new Source(url);  // 쓰레드를 사용 안하면 여기에서 예외 발생함 그 이유는 아래에서 설명
            Element element = null;
//            List<Element> list = source.getAllElements(HTMLElementName.SCRIPT); // title 태그의 엘리먼트 가져옴
            //   array.add(source.toString());
            encryx = source.toString();
            encryx = encryx.substring(encryx.indexOf("y")+3,encryx.length()-25);
            aaaaaa=1;
            trans = Double.parseDouble(encryx);
            trans = trans / 0.4;
            encryx = Integer.toString((int)trans);

            encryy = source.toString();

            encryy = encryy.substring(encryy.indexOf("x")+3,encryy.length()-2);

            aaaaaa=1;
            trans = Double.parseDouble(encryy);
            trans = trans / 0.4;
            encryy = Integer.toString((int)trans);

            String tmp;
            tmp = encryx;
            encryx = encryy;
            encryy = tmp;

            aaaaaa=111;

        }catch(Exception e){
            System.out.print("11");
        }
        return destinationx;  // 입력된 배열값을 리턴
    }

    /**
     * 전달된 인텐트 처리
     */
    public void setData(String des, double x, double y){
        //임시
        destinationmap = des;
        StartX = x;
        StartY = y;
        //임시
    }
    public ArrayList<pathListItem> recallPathListItem(ArrayList<String> array){
        int index = 0, length = array.size(), tmp = 0, ttmp = 0, idx = 0;
        ArrayList<pathListItem> PLI = new ArrayList<pathListItem>();
        String tmpMent = "", px = "", py = "" ;
        while(length > 1){
            while(ttmp != 3){
                if(array.get(index) == "\n"){ // "\n" 개행 걸러내기
                    Log.e("DO","nothing : " + index);
                    if((index == 0) || (index == array.size() - 5)){  //현재 보고있는 인덱스가 첫뻔째 그룹과 마지막 그룹일 경우를 걸러냄
                        tmp = 1;
                    }
                }else{
                    switch (tmp){
                        case 0:
                            if(ttmp == 0){
                                tmpMent = array.get(index).substring(8, array.get(index).length());   //순수하게 멘트만 받아오기
                                tmpMent = tmpMent.substring(0, tmpMent.indexOf('\''));
                                ttmp = ttmp + 1;
                            } else if(ttmp == 1) {
                                px = array.get(index).substring(5, array.get(index).length());   //순수한 x좌표만 받아오기
                                px = px.substring(0, px.indexOf('\''));
                                ttmp = ttmp + 1;
                                index = index + 1;
                                length = length - 1;
                            } else if(ttmp == 2) {
                                py = array.get(index).substring(5, array.get(index).length());   //순수한 y좌표만 받아오기
                                py = py.substring(0, py.indexOf('\''));
                                ttmp = ttmp + 1;
                            }
                            break;
                        case 1:
                            if(ttmp == 0){
                                tmpMent = array.get(index).substring(8, array.get(index).length());   //순수하게 멘트만 받아오기
                                tmpMent = tmpMent.substring(0, tmpMent.indexOf('\''));
                                ttmp = ttmp + 1;
                            } else if(ttmp == 1){
                                px = array.get(index).substring(5, array.get(index).length());   //순수한 x좌표만 받아오기
                                px = px.substring(0, px.indexOf('\''));
                                ttmp = ttmp + 1;
                            }else if(ttmp == 2){
                                py = array.get(index).substring(5, array.get(index).length());   //순수한 y좌표만 받아오기
                                py = py.substring(0, py.indexOf('\''));
                                ttmp = ttmp + 1;
                            }//처음과 끝
                            break;
                    }
                    // 9 번 index
                }
                index = index + 1;
                length = length - 1;
            }
            PLI.add(new pathListItem(idx, tmpMent, Double.parseDouble(px), Double.parseDouble(py)));
            tmpMent = "";
            px = "";
            py = "";
            ttmp = 0;
            tmp = 0;
            idx = idx + 1;
        }
        return PLI;
    }

}