// 基于准备好的dom，初始化echarts实例
let myChart = null
console.log(document.getElementById('doc_vec_dom'))
$(document).ready(function (){
     myChart = echarts.init(document.getElementById('doc_vec_dom'));

})




//第二步构造数据
var data1 = []; //数据区域缩放组件
var data2 = [];
var data3 = [];

function drawWord2Vec(res, limit) {
    data1 = []
    data2 = []
    data3 = []
    let data_json = res
    console.log(data_json)
   // var max_time = res.max_time
    //var min_time = res.min_time
    for(var d = 0; d < data_json.px.length; d++){
        if(d > limit){
            break;
        }


        data1.push([parseFloat(data_json.px[d]),
                parseFloat(data_json.py[d]),
                d])
    }


    //3、配置option项
    //第三步就是使用echarts的option进行参数的配置
    let option = {
        animation: true,//是否开启动画，默认是开启的，true是开启的,false是关闭的

        //图例
        legend: {
            data: [
                {
                    name:'scatter',
                    icon:'circle',//强制设置图形长方形
                    textStyle:
                        {color:'red'}//设置文本为红色
                },
            ],
            zlevel:5,//设置Canvas分层 zlevel值不同会被放在不同的动画效果中,传说中z值小的图形会被z值大的图形覆盖
            z:3,//z的级别比zlevel低，传说中z值小的会被z值大的覆盖，但不会重新创建Canvas
            left:'center',//图例组件离容器左侧的距离。可以是像 '20%' 这样相对于容器高宽的百分比，也可以是 'left', 'center', 'right'。
            //如果 left 的值为'left', 'center', 'right'，组件会根据相应的位置自动对齐。
            top:'top',
            //rigth:'',
            //bottom:''
            width:'auto',//设置图例组件的宽度，默认值为auto,好像只能够使用px
            orient:'horizontal',//设置图例组件的朝向默认是horizontal水平布局，'vertical'垂直布局
            align:'auto',//'left'  'right'设置图例标记和文本的对齐，默认是auto
            //默认自动，根据组件的位置和 orient 决定，当组件的 left 值为 'right' 以及纵向布局（orient 为 //'vertical'）的时候为右对齐，及为 'right'。
            padding:[20,20,20,20],//设置图例内边距 默认为上下左右都是5，接受数组分别设定
            itemGap:20,//图例每项之间的间隔，横向布局时为水平间隔，纵向布局时为纵向间隔。默认为10
            itemWidth:30,//图例标记的图形宽度，默认为25
            itemHeight:20,//图例标记的图形高度，默认为14
            //formatter:'Legend {name}'//图例文本的内容格式器，支持字符串模板和回调函数两种形式。
            formatter:function(name){
                return 'Legend  '+name;
            },
            //selectedMode:false,//图例的选择模式，默认为开启，也可以设置成single或者multiple
            //selectedMode:'single'//图例的选择模式，默认为开启，也可以设置成single或者multiple
            selectedMode:'multiple',//图例的选择模式，默认为开启，也可以设置成single或者multiple
            inactiveColor:'#ccc',//图例关闭时的颜色，默认是'#ccc'
            selected:{
                'scatter': true,
            },
            tooltip: {//图例的tooltip 配置，默认不显示,可以在文件较多的时候开启tooltip对文字进行剪切
                show: true,
            },
            //backgroundColor:'rgb(128, 128, 128)',//图例的背景颜色，默认为透明的
            //borderColor:'rgb(10, 150, 200)',//图例的边框颜色
            //borderWidth:2,//图列的边框线宽，默认为1
            shadowBlur:30,//图例阴影的模糊大小
            shadowColor:'rgb(128, 128, 56)'//阴影的颜色
        },

        //鼠标移上去的提示
        tooltip: {
            formatter: function(param) {

                const value = param.value;
                var str = '<div style="border-bottom: 1px solid rgba(255,255,255,.3); font-size: 12px;padding-bottom: 7px;margin-bottom: 7px;"> '
                    +'index : ' + value[2] + '</div> <div>'      + '( ' + value[0] + ',' + value[1] + ') </div>' + '<div>' +
                     files[parseInt(param.value[2])] +
                    '</div>';

                return str;




            },

        },

        //网格
        grid:{
            show:true,//是否显示直角坐标系的网格,true显示，false不显示
            left:100,//grid组件离容器左侧的距离
            containLabel:false,//grid 区域是否包含坐标轴的刻度标签，在无法确定坐标轴标签的宽度，容器有比较小无法预留较多空间的时候，可以设为 true 防止标签溢出容器。

        },

        //X轴
        xAxis: {
            type: 'value',
            min: 'dataMin',
            name:'xAxis',//x轴的名称
            nameLocation:'end',//x轴名称的显示位置'middle'，'end'
            nameRotate:30,//坐标轴名字旋转角度值
            max: 'dataMax',
            gridIndex:0,//x轴所在的grid的索引，默认位于第一个grid
            //offset:10,//x轴相对默认位置的偏移，在一个grid中含有多个grid的时候有意义
            type:'value',//数值轴适用于连续型数据
            //'category'类目轴 适用于离散的类目数据，为该类型时必须通过 data 设置类目数据。
            //'time' //时间轴，适用于连续的时序数据，与数值轴相比时间轴带有时间的格式化，在刻度计算上也有所不同，例如会根据跨度的范围来决定使用月，星期，日还是小时范围的刻度。
            //'log' 对数轴。适用于对数数据。
            position:'top',//x轴位于grid的上方还是下方，默认为bottom在下方
            inverse:false,//是否反向坐标
            boundaryGap:['20%','20%'],//坐标轴两边留白策略，类目轴和非类目轴的设置和表现不一样。
            //类目轴中 boundaryGap 可以配置为 true 和 false。默认为 true，这时候刻度只是作为分隔线，标签和数据点都会在两个刻度之间的带(band)中间。
            //非类目轴，包括时间，数值，对数轴，boundaryGap是一个两个值的数组，分别表示数据最小值和最大值的延伸范围，可以直接设置数值或者相对的百分比，在设置 min 和 max 后无效
            splitLine: {
                show: true
            }
        },

        //Y轴
        yAxis: {
            type: 'value',
            min: 'dataMin',
            max: 'dataMax',
            splitLine: {
                show: true
            }
        },

        //数据区域缩放、滚动条
        dataZoom: [
            {
                type: 'slider',
                show: true,
                xAxisIndex: [0],
                start: 1,
                end: 35
            },
            {
                type: 'slider',
                show: true,
                yAxisIndex: [0],
                left: '93%',
                start: 29,
                end: 36
            },
            {
                type: 'inside',
                xAxisIndex: [0],
                start: 1,
                end: 35
            },
            {
                type: 'inside',
                yAxisIndex: [0],
                start: 29,
                end: 36
            }
        ],

        //visualMap
        visualMap: {
            //type: 'continuous',//类型为连续型。
            min: -100,//指定 visualMapContinuous 组件的允许的最小值。'min' 必须用户指定。[visualMap.min, visualMax.max] 形成了视觉映射的『定义域』。
            max: 100,//指定 visualMapContinuous 组件的允许的最大值。'max' 必须用户指定。[visualMap.min, visualMax.max] 形成了视觉映射的『定义域』。
            //setOption 改变 min、max 时 range 的自适应
            text:['High','Low'],//两端的文本['High', 'Low']
            range:[0, 800],//指定手柄对应数值的位置
            calculable:true,//表示是否设置了拖拽用的手柄，如果为false则拖拽结束时，才更新视图。如果为ture则拖拽手柄过程中实时更新图表视图。
            realtime: false,//表示拖拽时是否实时更新
            inverse:false,//是否翻转组件
            precision:2,//展式的小数点精度
            itemWidth:30,//长条的宽度，默认为20
            itemHeight:200,//长条的高度，默认是140
            align:'left',//组件中手柄和文字的摆放位置'auto'   (orient 为 horizontal 时有效) 'left'  'right'    （rient 为 vertical 时有效。）'top' 'bottom'
            textGap:20,//两端文字与主体之间的距离
            show:true,//是否显示数据映射组件
            dimension:0,//指定用数据的『哪个维度』，映射到视觉元素上。『数据』即 series.data。 可以把 series.data 理解成一个二维数组，
            seriesIndex:0,//指定去哪个系列的数据
            hoverLink:true,//打开 hoverLink 功能时，鼠标悬浮到 visualMap 组件上时，鼠标位置对应的数值 在 图表中对应的图形元素，会高亮。
            //反之，鼠标悬浮到图表中的图形元素上时，在 visualMap 组件的相应位置会有三角提示其所对应的数值。
            //inRange: {//能定义目标系列（参见 visualMap-continuous.seriesIndex）视觉形式，也同时定义了 visualMap-continuous 本身的视觉样式。
            //视觉通道
            //inRange 中，可以有任意几个的『视觉通道』定义（如 color、symbolSize 等）。这些视觉通道，会被同时采用。
            //一般来说，建议使用 透明度（opacity） ，而非 颜色透明度（colorAlpha） （他们细微的差异在于，前者能也同时控制图元中的附属物（如 label）的透明度，而后者只能控制图元本身颜色的透明度）。
            //视觉映射的方式：支持两种方式：线性映射、查表映射。
            //线性映射 表示 series.data 中的每一个值（dataValue）会经过线性映射计算，从 [visualMap.min, visualMap.max] 映射到设定的 [visual value 1, visual value 2] 区间中的某一个视觉的值（下称 visual value）。
            //查表映射 表示 series.data 中的所有值（dataValue）是可枚举的，会根据给定的映射表查表得到映射结果。
            /*color: ['#121122', 'rgba(3,4,5,0.4)', 'red'],
             symbolSize: [30, 100]//图元的大小。*/
            // 表示 目标系列 的视觉样式。

            //},
            /*target: {
             inRange: {
             symbol: 图元的图形类别。
             symbolSize: 图元的大小。
             color: 图元的颜色。
             colorAlpha: 图元的颜色的透明度。
             opacity: 图元以及其附属物（如文字标签）的透明度。
             colorLightness: 颜色的明暗度，参见 HSL。
             colorSaturation: 颜色的饱和度，参见 HSL。
             colorHue: 颜色的色调，参见 HSL。
             color: ['#121122', 'rgba(3,4,5,0.4)', 'red'],
             symbolSize: [60, 200]

             }
             },*/
            // 表示 visualMap-continuous 本身的视觉样式。
            /*controller: {visualMap 组件中，控制器 的 inRange outOfRange 设置。如果没有这个 controller     设置，控制器 会使用外层的 inRange outOfRange 设置；如果有这个 controller 设置，则会采用这个设置。适用于一些控制器视觉效果需要特殊定制或调整的场景。
             inRange: {
             symbolSize: [30, 100],
             symbol:['circle', 'rect', 'diamond'],
             color: ['#121122', 'rgba(3,4,5,0.4)', 'red']
             }
             },*/
            type:'piecewise',//表示分段型视觉映射组件（visualMapPiecewise）
            splitNumber:10,//对于连续型数据，自动切分成几段默认是5段
            pieces: [//每一段的范围，以及每一段的文字，以及每一段的特别的样式
                {min: 1500}, // 不指定 max，表示 max 为无限大（Infinity）。
                {min: 900, max: 1500},
                {min: 310, max: 1000},
                {min: 200, max: 300},
                {min: 10, max: 200, label: '10 到 200（自定义label）'},
                {value: 123, label: '123（自定义特殊颜色）', color: 'grey'}, // 表示 value 等于 123 的情况。
                {max: 5}     // 不指定 min，表示 min 为无限大（-Infinity）。
            ],
            min:2,//指定 visualMapPiecewise 组件的最小值。
            max:5,//指定 visualMapPiecewise 组件的最大值。
            selectedMode:'multiple',//(多选)(单选)
            inverse:false,//是否反转
            precision:2,//数据展示的小数精度
            itemWidth:20,//图形的宽度，即每个小块的宽度。
            itemHeight:20,//图形的高度，即每个小块的高度
            text:['High', 'Low'],//两端的文本
            align:'auto',// 自动决定。'left' 图形在左文字在右。'right' 图形在右文字在左。
            textGap:20,//两端文字主体之间的距离
            itemGap:10,//每两个图元之间的间隔距离，单位为px。
            itemSymbol:'circle',//图形的形状 'rect', 'roundRect', 'triangle', 'diamond', 'pin', 'arrow'
            show:true,//是否显示组件
            dimension:0,//指定用数据的『哪个维度』，映射到视觉元素上。
            seriesIndex:0,//指定取哪个系列的数据，即哪个系列的 series.data。
            hoverLink:true,//打开 hoverLink 功能时，鼠标悬浮到 visualMap 组件上时，鼠标位置对应的数值 在 图表中对应的图形元素，会高亮。
            //inRange://参考visualMapContinuous
            //outOfRange://参考visualMapContinuous*/
            /*categories: [//用于表示离散型数据（或可以称为类别型数据、枚举型数据）的全集。
             'Demon Hunter', 'Blademaster', 'Death Knight', 'Warden', 'Paladin'
             ],*/
            /*inRange: {
             // visual value 可以配成 Object：
             color: {
             'Warden': 'red',
             'Demon Hunter': 'black',
             '': 'green' // 空字串，表示除了'Warden'、'Demon Hunter'外，都对应到 'green'。
             },
             // visual value 也可以只配一个单值，表示所有都映射到一个值，如：
             color: 'green',
             // visual value 也可以配成数组，这个数组须和 categories 数组等长，
             // 每个数组项和 categories 数组项一一对应：
             color: ['red', 'black', 'green', 'yellow', 'white']
             },*/
            /*outOfRange:{//定义 在选中范围外 的视觉元素。
             color: ['#121122', 'rgba(3,4,5,0.4)', 'red'],
             symbolSize: [30, 100]
             },
             olor: ['orangered','yellow','lightskyblue']*/
        },


        //装载数据
        series: [
            {
                name: 'scatter',
                type: 'scatter',
                itemStyle: {
                    normal: {
                        opacity: 0.8,
                        label: {
                            color: '#7B38F8',

                            show: true,
                            position: 'top',
                            formatter: function (params, ticket, callback) {
                                return params.data[2];
                            }
                        },
                        x:data1[0],
                        y:data1[1],
                        textStyle: { fontSize: 12 }
                    }
                },
                symbolSize: 50,
                data: data1
            },


        ]
    };


    // 使用刚指定的配置项和数据显示图表
    myChart.setOption(option);
    myChart.on('click', function (parmas){
        console.log(parmas.value[2])
        showSourceCode(parmas.value[2])
    })

}


