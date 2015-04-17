﻿

/// <reference path="~/Scripts/BMap.js" />
/// <reference path="~/Scripts/bmaplibrary.js" />


/**
* 地图命名空间
* 
*/
var ExpLink = ExpLink || {};

/**
* 地图命名空间中添加对全局window的引用
*/
ExpLink.global = this;

/**
* 标示是否是调试状态
*/
ExpLink.DEBUG = true;
/**
* 基础方法。此类中方法用于实现继承、类型判断等等。 
*/

/**
* 基本。此类中方法用于实现继承、类型判断等等。 
*/
ExpLink.Utility = function ()
{ }
/**
* 继承
*/
ExpLink.Utility.inherits = function (childCtor, parentCtor)
{
    /** @构造 */
    function tempCtor() { };
    tempCtor.prototype = parentCtor.prototype;
    childCtor.superClass_ = parentCtor.prototype;
    childCtor.prototype = new tempCtor();
    /** @重新定义构造 */
    childCtor.prototype.constructor = childCtor;
    /** @定义一个可以访问超类中方法的方法 */
    childCtor.base = function (me, methodName, var_args)
    {
        var args = Array.prototype.slice.call(arguments, 2);
        return parentCtor.prototype[methodName].apply(me, args);
    };
};

/**
* 执行基类构造函数
*/
ExpLink.Utility.base = function (me, opt_methodName, var_args)
{
    var caller = arguments.callee.caller;

    if (ExpLink.DEBUG)
    {
        if (!caller)
        {
            throw Error("无法调用方法执行者");
        }
    }
    if (caller.superClass_)
    {
        // 调用基类构造.
        return caller.superClass_.constructor.apply(
            me, Array.prototype.slice.call(arguments, 1));
    }

    var args = Array.prototype.slice.call(arguments, 2);
    var foundCaller = false;
    for (var ctor = me.constructor;
         ctor; ctor = ctor.superClass_ && ctor.superClass_.constructor)
    {
        if (ctor.prototype[opt_methodName] === caller)
        {
            foundCaller = true;
        } else if (foundCaller)
        {
            return ctor.prototype[opt_methodName].apply(me, args);
        }
    }
    // 处理无法找到调用者的异常情况。
    if (me[opt_methodName] === caller)
    {
        return me.constructor.prototype[opt_methodName].apply(me, args);
    } else
    {
        throw Error("调用失败");
    }
};

/**
*判断变量是否定义
*/
ExpLink.Utility.isDef = function (val)
{
    return val != undefined;
}

/**
*判断变量是否初始化
*/
ExpLink.Utility.isNull = function (val)
{
    return val === null;
}

/**
*判断变量是否定义及初始化
*/
ExpLink.Utility.isDefAndNotNull = function (val)
{
    return val != null;
};

/**
*判断变量类型
*/
ExpLink.Utility.typeOf = function (value)
{
    var s = typeof value;
    if (s == 'object')
    {
        if (value)
        {
            if (value instanceof Array)
            {
                return 'array';
            } else if (value instanceof Object)
            {
                return s;
            }
            var className = Object.prototype.toString.call((value));
            if (className == '[object Window]')
            {
                return 'object';
            }
            if ((className == '[object Array]' ||
                 typeof value.length == 'number' &&
                 typeof value.splice != 'undefined' &&
                 typeof value.propertyIsEnumerable != 'undefined' &&
                 !value.propertyIsEnumerable('splice')
                ))
            {
                return 'array';
            }
            if ((className == '[object Function]' ||
                typeof value.call != 'undefined' &&
                typeof value.propertyIsEnumerable != 'undefined' &&
                !value.propertyIsEnumerable('call')))
            {
                return 'function';
            }
        } else
        {
            return 'null';
        }
    } else if (s == 'function' && typeof value.call == 'undefined')
    {
        return 'object';
    }
    return s;
};


/**
*判断变量是否为数组类型
*/
ExpLink.Utility.isArray = function (val)
{
    return ExpLink.Utility.typeOf(val) == 'array';
};

/**
 * 字符串
 */
ExpLink.Utility.isString = function (val)
{
    return typeof val == 'string';
};


/**
 * 布尔类型
 */
ExpLink.Utility.isBoolean = function (val)
{
    return typeof val == 'boolean';
};


/**
 * 数字.
 */
ExpLink.Utility.isNumber = function (val)
{
    return typeof val == 'number';
};


/**
 * 方法
 */
ExpLink.Utility.isFunction = function (val)
{
    return ExpLink.Utility.typeOf(val) == 'function';
};

/**
 * 基类对象
 */
ExpLink.Utility.isObject = function (val)
{
    var type = typeof val;
    return type == 'object' && val != null || type == 'function';
};


/**
* 将字符串转换成对象
* 通过try/catch实现异常捕获，效率是最低的。
* 默认JSON可用
*/
ExpLink.Utility.parse = function (val)
{
    if (!ExpLink.Utility.isDefAndNotNull(val))
    {
        return false;
    }

    try
    {
        var result = JSON.parse(val);
        return { status: true, result: result };
    } catch (e)
    {
        return { status: false, result: null };
    }
}

/**
 * 创建一个GUID
 */
ExpLink.Utility.guid = function ()
{
    return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c)
    {
        var r = Math.random() * 16 | 0, v = c == 'x' ? r : (r & 0x3 | 0x8);
        return v.toString(16);
    });
};



/////////////////// 事件
/**
 * 事件对象
 */
ExpLink.Event = function (type, target)
{
    this.type = type;
    this.returnValue = true;
    this.target = target || null;
    this.currentTarget = null;
};

////////////// 观察者对象
/**
 * 事件对象
 */
ExpLink.Observable = function ()
{

}
/**
 * 添加事件
 */
ExpLink.Observable.prototype.add = function (type, handler, key)
{
    if (!ExpLink.Utility.isFunction(handler))
    {
        return;
    }
    !this.__listeners && (this.__listeners = {});
    var t = this.__listeners, id;
    if (typeof key == "string" && key)
    {
        if (/[^\w\-]/.test(key))
        {
            throw ("nonstandard key:" + key);
        }
        else
        {
            handler.hashCode = key;
            id = key;
        }
    }
    type.indexOf("on") != 0 && (type = "on" + type);
    typeof t[type] != "object" && (t[type] = {});
    id = id || ExpLink.Utility.guid();
    handler.hashCode = id;
    t[type][id] = handler;

    return id;
};
/**
 * 移除事件
 */
ExpLink.Observable.prototype.remove = function (type, handler)
{
    if (ExpLink.Utility.isFunction(handler))
    {
        handler = handler.hashCode;
    }
    else if (!ExpLink.Utility.isString(handler))
    {
        return;
    }
    !this.__listeners && (this.__listeners = {});
    type.indexOf("on") != 0 && (type = "on" + type);
    var t = this.__listeners;
    if (!t[type])
    {
        return;
    }
    t[type][handler] && delete t[type][handler];
};
/**
 * 分发事件
 */
ExpLink.Observable.prototype.dispatch = function (event, options)
{
    if (ExpLink.Utility.isString(event))
    {
        event = new ExpLink.Event(event);
    }
    !this.__listeners && (this.__listeners = {});
    options = options || {};
    for (var i in options)
    {
        event[i] = options[i];
    }
    var i, t = this.__listeners, p = event.type;
    event.target = event.target || this;
    event.currentTarget = this;
    p.indexOf("on") != 0 && (p = "on" + p);
    ExpLink.Utility.isFunction(this[p]) && this[p].apply(this, arguments);
    if (typeof t[p] == "object")
    {
        for (i in t[p])
        {
            t[p][i].apply(this, arguments);
        }
    }
    return event.returnValue;
};



/**
* 计算方法
*/
ExpLink.algo = function ()
{ }

/**
* 返回两个数组的交集和差集
* result.inter  :  交集
* result.diff   :  差集  1-2
*/
ExpLink.algo.interdiff = function (firstArr, firstFn, secondArr, secondFn)
{
    if (!ExpLink.Utility.isDefAndNotNull(firstArr) || !ExpLink.Utility.isArray(firstArr))
    {
        return null;
    }
    if (!ExpLink.Utility.isDefAndNotNull(secondArr) || !ExpLink.Utility.isArray(secondArr))
    {
        return firstArr;
    }
    if (!ExpLink.Utility.isDefAndNotNull(firstFn))
    {
        firstFn = function (ele) { return ele };
    }
    if (!ExpLink.Utility.isDefAndNotNull(secondFn))
    {
        secondFn = firstFn;
    }

    var resultInterArr = [];
    var resultDiffArr = [];
    var dic = {};
    for (var i = 0, length = secondArr.length; i < length; i++)
    {
        var item = secondFn.call(secondArr[i], secondArr[i]);
        dic[item] = true;
    }

    for (var i = 0, length = firstArr.length; i < length; i++)
    {
        var item = firstFn.call(firstArr[i], firstArr[i]);
        if (dic[item])
        {
            resultInterArr.push(firstArr[i]);
        }
        else
        {
            resultDiffArr.push(firstArr[i]);
        }
    }

    return { inter: resultInterArr, diff: resultDiffArr };
}


/**
* 模拟Linq
* data 只接受数组哦
*/
ExpLink.algo.linq = function (data)
{
    return new ExpLink.algo.linq.Enumberable(data);
};

/**
* 可枚举对象
* 暂时未实现 next 等方法
*/
ExpLink.algo.linq.Enumberable = function (data)
{
    this._dataItem = data;
}

/**
* 返回数组对象
* 此方法不返回对象本身
*/
ExpLink.algo.linq.Enumberable.prototype.toArray = function ()
{
    return this._dataItem;
}

/**
* 计算数组的差集
* 此方法返回枚举对象本身
*/
ExpLink.algo.linq.Enumberable.prototype.difference = function (otherArr, firstFn, secondFn)
{
    if (!ExpLink.Utility.isDefAndNotNull(otherArr) || !ExpLink.Utility.isArray(otherArr))
    {
        return this;
    }
    if (!ExpLink.Utility.isDefAndNotNull(firstFn))
    {
        firstFn = function (ele) { return ele };
    }
    if (!ExpLink.Utility.isDefAndNotNull(secondFn))
    {
        secondFn = firstFn;
    }

    var resultArr = [];
    var dic = {};
    for (var i = 0, length = otherArr.length; i < length; i++)
    {
        var item = secondFn.call(otherArr[i], otherArr[i]);
        dic[item] = true;
    }

    for (var i = 0, length = this._dataItem.length; i < length; i++)
    {
        var item = firstFn.call(this._dataItem[i], this._dataItem[i]);
        if (!dic[item])
        {
            resultArr.push(this._dataItem[i]);
        }
    }

    this._dataItem = resultArr;

    return this;
}




/**
* 地图管理对象
* 此对象同时也会管理一些具体的地图交互业务模块
*/
ExpLink.ExpdopMap = function ()
{
    // 调用基类构造
    ExpLink.Utility.base(this, arguments);

    this._regionManager = null;
    this._expdopMarkerManager = null;
    this._shopManager = null;// 
    this._deliveryStation = null;


};
ExpLink.Utility.inherits(ExpLink.ExpdopMap, ExpLink.Observable);


/**
* 初始化地图对象
* 
*/
ExpLink.ExpdopMap.prototype.initializeMap = function (opts)
{
    // 初始化地图
    var mapPanel = opts.map ? opts.map : "map";
    this._map = new BMap.Map(mapPanel, { enableMapClick: false }); //
    this._map.enableScrollWheelZoom();
    this._map.highResolutionEnabled();

    //确定初始的视域(北京)
    var oriLngitude = 116.403874;
    var oriLatitude = 39.914889;
    var oriLevel = 12;
    this._map.centerAndZoom(new BMap.Point(oriLngitude, oriLatitude), oriLevel);

    //通过ip重新定位初始视域
    // 如果初始参数中对初始视域进行了赋值，那么使用参数值
    this.resetView();

    // 添加默认控件
    var navigate = new BMap.NavigationControl();   // 导航
    this._map.addControl(navigate);

    var mapType = new BMap.MapTypeControl();      // 地图显示类型
    this._map.addControl(mapType);

}

/**
* 根据ip调整视域
*/
ExpLink.ExpdopMap.prototype.resetView = function ()
{
    var map = this._map;
    var localCity = new BMap.LocalCity();
    localCity.get(function (result)
    {
        if (result)
        {
            map.centerAndZoom(new BMap.Point(result.center.lng, result.center.lat), result.level);
        }
    });
};

/**
* 重置地图中心点和缩放比例
*
*/
ExpLink.ExpdopMap.prototype.centerAndZoom = function (lng, lat, level)
{
    this._map.centerAndZoom(new BMap.Point(lng, lat), level);
}



///区域交互绘制方面内容
/**
*获取当前的地图对象
*/
ExpLink.ExpdopMap.prototype.getMap = function ()
{
    return this._map;
}

/**
* 开始多边形的绘制
* 在添加或者编辑时  只是初始化一个交互环境
* 如： 绘制出相关的已存在的区域；初始化一些变量值
*/
ExpLink.ExpdopMap.prototype.startDrawRegion = function (opts)
{
    // 
    if (this._regionManager == null)
    {
        // 这样用不好，该将代码单独出来
        //初始化多边形交互绘制对象
        this._regionManager = new ExpLink.ExpdopDrawRegionManager({ 'map': this._map });

        // 定义事件通知
        var expdopMap = this;
        this._regionManager.add(ExpLink.ExpdopDrawRegionManager.EventType.NEWPOLYGON, function (e)
        {
            expdopMap.dispatch(e);
        });
        this._regionManager.add(ExpLink.ExpdopDrawRegionManager.EventType.SAVEEDIT, function (e)
        {
            expdopMap.dispatch(e);
        });
        this._regionManager.add(ExpLink.ExpdopDrawRegionManager.EventType.WARNING, function (e)
        {
            expdopMap.dispatch(e);
        });
        this._regionManager.add(ExpLink.ExpdopDrawRegionManager.EventType.EDITING, function (e)
        {
            expdopMap.dispatch(e);
        });

    }
    // 调整视域
    this.resetView();

    // 获取参数
    var existsOverlay = [];
    if (opts && opts.exists)
    {
        existsOverlay = opts.exists;
    }
    var overlay = "";
    if (opts && opts.overlay)
    {
        overlay = opts.overlay;
    }
    // 开始
    this._regionManager.start({ 'exists': existsOverlay, 'overlay': overlay });

}

/**
* 开始绘制 或者重绘重绘
* 只用于添加
* 如果是重绘，那么将清除之前绘制的结果
*/
ExpLink.ExpdopMap.prototype.reDraw = function ()
{
    if (this._regionManager == null)
    {
        return;
    }
    this._regionManager.reDraw();
}

/**
* 结束多边形的绘制
* 会进行清理工作
*/
ExpLink.ExpdopMap.prototype.stopDrawRegion = function ()
{

    if (this._regionManager == null)
    {
        return;
    }
    this._regionManager.end();
}

/**
* 编辑当前多边形对象
* 编辑当前选中的对象
*/
ExpLink.ExpdopMap.prototype.editDrawRegion = function ()
{
    if (this._regionManager == null)
    {
        return;
    }
    this._regionManager.edit();
}


/**
* 保存编辑
* 
*/
ExpLink.ExpdopMap.prototype.saveEditDrawRegion = function ()
{
    if (this._regionManager == null)
    {
        return;
    }
    this._regionManager.saveEdit();
}

/**
* 退出当前编辑状态不进行任何保存
*
*/
ExpLink.ExpdopMap.prototype.endWithoutSave = function ()
{
    if (this._regionManager == null)
    {
        return;
    }
    this._regionManager.endWithoutSave();
}


/**
* 获取新绘制的区域
* 返回值是一个区域对象
*/
ExpLink.ExpdopMap.prototype.getNewDrawRegion = function ()
{
    if (this._regionManager == null)
    {
        return;
    }
    return this._regionManager.getNewDrawRegion();
}


/**
* 设置多边形选中时的风格
* 绘制中和选中两种状态
*/
ExpLink.ExpdopMap.prototype.setSelectedStyleOptions = function (opts)
{
    if (this._regionManager == null)
    {
        return;
    }
    this._regionManager.setSelectedStyleOptions(opts);
}
/**
* 设置系统默认的绘制风格。
* 如果多边形本身没有附带风格参数，则使用此方法配置的风格。
*/
ExpLink.ExpdopMap.prototype.setDefaultStyleOptions = function (opts)
{
    if (this._regionManager == null)
    {
        return;
    }
    this._regionManager.setDefaultStyleOptions(opts);
}

/**
*清除地图中所有的覆盖物
*/
ExpLink.ExpdopMap.prototype.clearOverlays = function ()
{
    this._map.clearOverlays();
}


/// 监控以及分单操作

/**
* 初始化监控以及分单管理器
*/
ExpLink.ExpdopMap.prototype.InitializeMarkerManager = function ()
{
    this._expdopMarkerManager = new ExpLink.ExpdopMarkerManager({ map: this._map });

    // 添加事件
    var expdopMap = this;
    this._expdopMarkerManager.add("shopclick", function (e)
    {
        expdopMap.dispatch(e);
    });
    this._expdopMarkerManager.add("courierclick", function (e)
    {
        expdopMap.dispatch(e);
    });

}

/**
* 获取监控以及分单管理器对象
*/
ExpLink.ExpdopMap.prototype.getMarkerManager = function ()
{
    return this._expdopMarkerManager;
}




/// 店面管理-- 店面信息维护

/**
* 初始化店面管理器
*
*/
ExpLink.ExpdopMap.prototype.initializeShopManager = function ()
{
    this._shopManager = new ExpLink.ExpdopShopManager({ map: this._map });

    var expdopMap = this;

    // 绑定交互事件
    this._shopManager.add(ExpLink.ShopManagerEventType.NEWPICK, function (e)
    {
        expdopMap.dispatch(e);
    });

    this._shopManager.add(ExpLink.ShopManagerEventType.TARGETSELECTED, function (e)
    {
        expdopMap.dispatch(e);
    });
}

/**
* 获取店面管理器对象
*/
ExpLink.ExpdopMap.prototype.getShopManager = function ()
{
    return this._shopManager;
};




/**
* 站点分单
*/
ExpLink.ExpdopMap.prototype.initializeDeliveryStation = function ()
{
    this._deliveryStation = new ExpLink.DeliveryStation({ map: this._map });

    var _this = this;

    // 绑定交互事件
    this._deliveryStation.add(ExpLink.DeliveryStationEventType.STATIONREGIONCLICK, function (e)
    {
        _this.dispatch(e);
    });

}

/**
* 获取店面管理器对象
*/
ExpLink.ExpdopMap.prototype.getDeliveryStation = function ()
{
    return this._deliveryStation;
};









// 图形绘制
/**
* 图形绘制管理器，依赖百度开源库
* 现只有多边形一种
* 这个模块现在还存在一些逻辑bug，如编辑和添加不同状态时的一些函数是否可用等的判断。
*       这些问题计划通过页面元素来避免
*/
ExpLink.ExpdopDrawRegionManager = function (opts)
{
    ExpLink.Utility.base(this);

    this._map = opts.map ? opts.map : null;    // 地图对象
    this._currentPolygon = null;               // 当前编辑的对象
    this._drawingManager = null;               // 地图绘制交互管理器
    this._existsPolygon = [];                  // 存在的多边形  1.新建：包含相关的多边形  2：编辑：相关以及编辑的对象本身
    this._currentSelectedPolygon = null;       // 默认当前选中的多边形 ； 主要用于渲染状态的还原
    this._currentDrawStyleOptions = null;      // 当前交互绘制过程中的多边形对象的渲染风格。
    this._currentShowStyleOptions = null;      // 当前多边形绘制结果的渲染风格。
    this._newPolygon = null;                   // 新绘制的多边形（只在添加模式下有存在意义）;
    this._newPolygonID = null;                   // 添加多边形时的ID，此ID只在添加模式时存在，修改模式时不存在此ID


    this._initialize();
}
ExpLink.Utility.inherits(ExpLink.ExpdopDrawRegionManager, ExpLink.Observable);

/**
* 事件类型
*/
ExpLink.ExpdopDrawRegionManager.EventType = {
    SAVEEDIT: "saveedit",   // 保存编辑结果
    NEWPOLYGON: "newpolygon", // 事件类型
    WARNING: "warning",         // 警告以及操作提示
    EDITING: "editing"           // 当前正在编辑的对象的风格
};


/**
* 初始化地图绘制状态
* 
*/
ExpLink.ExpdopDrawRegionManager.prototype._initialize = function ()
{
    // 清空地图的覆盖层，确保绘图区域干净
    if (ExpLink.Utility.isDefAndNotNull(this._map))
    {
        this._map.clearOverlays();
    }

    // 获取默认的绘制风格
    this._currentDrawStyleOptions = this._getStyleOptions();      // 
    // 获取默认的显示风格
    this._currentShowStyleOptions = this._getExistsOptions();      // 

    // 创建绘制交互对象
    this._drawingManager = new BMapLib.DrawingManager(this._map, {
        isOpen: false, //是否开启绘制模式
        enableDrawingTool: false, //是否显示工具栏
        polygonOptions: this._currentDrawStyleOptions //多边形的样式
    });

    // 设置回调函数  
    // 绘制完成
    var drawingManager = this;
    this._drawingManager.addEventListener("overlaycomplete", function (e)
    {
        drawingManager._newPolygon = e.overlay; //当前绘制的结果对象
        // 添加自定义属性(使用当前的显示风格)
        drawingManager._newPolygon.selfStyleOptions = new ExpLink.lbs.Polygon(drawingManager._newPolygon.getPath(), drawingManager._newPolygonID, drawingManager._currentShowStyleOptions);
        // 添加到列表中
        drawingManager._existsPolygon.push(drawingManager._newPolygon);

        // 添加单击事件
        drawingManager._bindClickEvent(drawingManager._newPolygon);

        // 绑定右键菜单
        drawingManager._bindContextMenu(drawingManager._newPolygon);

        //改变当前选中的对象的渲染状态
        drawingManager._resetRenderStyles(drawingManager._newPolygon, drawingManager._newPolygon.selfStyleOptions.styleOptions);

        // 广播事件 : 绘制完成
        drawingManager.dispatch(new ExpLink.Event(ExpLink.ExpdopDrawRegionManager.EventType.NEWPOLYGON, drawingManager._newPolygon.selfStyleOptions));

    });

    // 地图点击事件
    // 主要用于处理当前地图中的要素非选中状态
    this._map.addEventListener("click", function (e)
    {
        // 覆盖物的点击事件会上传到地图，此判断条件可以排除点击的覆盖物
        if (!ExpLink.Utility.isDefAndNotNull(e.overlay) && drawingManager._currentSelectedPolygon != null && drawingManager._existsPolygon.indexOf(drawingManager._currentSelectedPolygon) != -1)
        {
            drawingManager._resetRenderStyles(drawingManager._currentSelectedPolygon, drawingManager._currentSelectedPolygon.selfStyleOptions.styleOptions);
            drawingManager._currentSelectedPolygon = null;
        }
    })
}

/**
* 开始绘制  // 
* 只是显示一些需要初始化显示的对象
*/
ExpLink.ExpdopDrawRegionManager.prototype.start = function (opts)
{
    ///重置变量状态
    this.endWithoutSave();


    // 添加相关区域的渲染
    if (opts.exists)
    {
        for (var i = 0, length = opts.exists.length; i < length; i++)
        {
            this._renderExists(opts.exists[i]);
        }
    }

    // 编辑已存在要素
    if (opts.overlay)
    {
        this._existEdit(opts.overlay);
    }

    // 设置绘图的状态
    if (ExpLink.Utility.isDefAndNotNull(this._drawingManager))
    {
        this._drawingManager.setDrawingMode(BMAP_DRAWING_POLYGON);
    }


}

/**
* 开始绘制 / 重绘
* 
*/
ExpLink.ExpdopDrawRegionManager.prototype.reDraw = function ()
{
    if (this._newPolygon != null)
    {
        // 地图中删除现存在的多边形
        this._map.removeOverlay(this._newPolygon);
        // 列表中删除
        // 
        var index = this._existsPolygon.lastIndexOf(this._newPolygon);
        if (index >= 0)
        {
            this._existsPolygon.splice(index, 1);
        }
        this._newPolygon = null;
        this._currentPolygon = null;
    }
    // 开始绘制
    this._drawingManager.open();

    // 此方法现在和end方法的配合还有bug.
    // 异常： 调用end后，不用调用start方法，此方法还是可以生效的
}

/**
* 结束绘图状态
* 
*/
ExpLink.ExpdopDrawRegionManager.prototype.end = function ()
{

    // 如果当前正处于绘制状态，那么默认保存编辑结果
    this.saveEdit();

    // 结束编辑状态
    this.endWithoutSave();

}

/**
* 结束绘图状态，不进行保存
*/
ExpLink.ExpdopDrawRegionManager.prototype.endWithoutSave = function ()
{
    // 首先清除掉现有的元素
    if (this._existsPolygon != null)
    {
        var length = this._existsPolygon.length
        for (var i = 0; i < length; i++)
        {
            this._map.removeOverlay(this._existsPolygon[i]);
        }
    }

    // 关闭交互编辑状态
    this._drawingManager.close();

    //将参数恢复默认值
    this._currentPolygon = null;
    this._existsPolygon = [];
    this._currentSelectedPolygon = null;
    this._newPolygon = null;
    this._newPolygonID = null;
}



/**
* 开始编辑
* 编辑当前选定的多边形
*/
ExpLink.ExpdopDrawRegionManager.prototype.edit = function ()
{
    if (this._currentPolygon != null)
    {// 正在进行编辑
        this._dispatchWarning("系统处于编辑状态，请先保存后再试！");
        return;
    }
    if (this._currentSelectedPolygon == null)
    {// 当前没有选中对象
        this._dispatchWarning("请先选中对象后再试！");
        return;
    }
    // 开始编辑当前选中对象
    this._currentSelectedPolygon.enableEditing();

    // 指定当前的编辑对象
    this._currentPolygon = this._currentSelectedPolygon;

    // 修改显示风格
    this._resetRenderStyles(this._currentPolygon, this._currentPolygon.selfStyleOptions.styleOptions);

    // 广播，将当前正在编辑对象的风格传出
    this.dispatch(new ExpLink.Event(ExpLink.ExpdopDrawRegionManager.EventType.EDITING, this._currentPolygon.selfStyleOptions.styleOptions));
}
/**
* 保存编辑结果
* 如果不保存，绘制结果是不会保留的
*/
ExpLink.ExpdopDrawRegionManager.prototype.saveEdit = function ()
{
    if (this._currentPolygon == null)
    {
        return;
    }
    //停止编辑状态
    this._currentPolygon.disableEditing();

    //记录新的坐标
    this._currentPolygon.selfStyleOptions.path = this._currentPolygon.getPath();

    // 重置外接矩形
    this._currentPolygon.selfStyleOptions.extent = ExpLink.lbs.Rectangle.fromPoints(this._currentPolygon.selfStyleOptions.path);
    // 重置中心点
    this._currentPolygon.selfStyleOptions.center = ExpLink.lbs.utility.getCenter(this._currentPolygon.selfStyleOptions.path);
    //记录新的渲染风格
    this._currentPolygon.selfStyleOptions.styleOptions = this._currentShowStyleOptions;

    // 广播编辑结果
    this.dispatch(new ExpLink.Event(ExpLink.ExpdopDrawRegionManager.EventType.SAVEEDIT, this._currentPolygon.selfStyleOptions));

    //将代表当前编辑对象的对象置空
    this._currentPolygon = null;


    // 选中对象也置空？  此操作的效果待测试。
    this._currentSelectedPolygon = null;
}

/**
* 设置新要素或者选中要素时 要素的渲染风格。
*
*/
ExpLink.ExpdopDrawRegionManager.prototype.setSelectedStyleOptions = function (opts)
{
    // 获取默认的绘制风格
    this._currentDrawStyleOptions = opts;      // 
}
/**
* 设置系统默认的绘制风格。 (默认，修改时的风格)
* 如果多边形本身没有附带风格参数，则使用此方法配置的风格。
* 此方法也可以用于修改当前多边形的风格
*/
ExpLink.ExpdopDrawRegionManager.prototype.setDefaultStyleOptions = function (opts)
{    // 
    // 获取默认的显示风格
    this._currentShowStyleOptions = opts;

    if (this._currentPolygon != null)  // 如果当前处于编辑状态，那么修改当前的多边形
    {
        this._resetRenderStyles(this._currentPolygon, opts);
    }

}


/**
* 获取新添加的多边形(返回的是参数)
*/
ExpLink.ExpdopDrawRegionManager.prototype.getNewDrawRegion = function ()
{
    if (this._newPolygon == null)
    {
        return null;
    }

    return this._newPolygon.selfStyleOptions;
}

/**
* 广播警告
* 将系统交互过程中的部分操作限制传出
*/
ExpLink.ExpdopDrawRegionManager.prototype._dispatchWarning = function (msg)
{
    this.dispatch(new ExpLink.Event(ExpLink.ExpdopDrawRegionManager.EventType.WARNING, msg));
}

/**
* 渲染存在的相关多边形
* polygonstr  ：  多边形的参数字符串
**/
ExpLink.ExpdopDrawRegionManager.prototype._renderExists = function (polygonstr)
{
    // 传入参数不可用
    if (!ExpLink.Utility.isDefAndNotNull(polygonstr))
    {
        return;
    }

    var bdpolygon = this._createPolygonFromParams(polygonstr);

    // 绑定点击事件
    this._bindClickEvent(bdpolygon);

    // 绑定右键菜单
    this._bindContextMenu(bdpolygon);

    //添加到地图中
    this._map.addOverlay(bdpolygon);
    this._existsPolygon.push(bdpolygon);

}

/**
* 编辑已存在的要素
*/
ExpLink.ExpdopDrawRegionManager.prototype._existEdit = function (overlay)
{
    // 传入参数不可用
    if (!ExpLink.Utility.isDefAndNotNull(overlay))
    {
        return;
    }

    // 创建多边形，
    var bdpolygon = this._createPolygonFromParams(overlay);

    // 绑定点击事件
    this._bindClickEvent(bdpolygon);

    // 绑定右键菜单
    this._bindContextMenu(bdpolygon);

    // 选中当前多边形
    this._currentSelectedPolygon = bdpolygon;
    this._resetRenderStyles(this._currentSelectedPolygon, this._currentDrawStyleOptions);

    // 添加到地图中
    this._existsPolygon.push(bdpolygon);
    this._map.addOverlay(bdpolygon);

    // 地图视域定位
    var map = this._map;
    setTimeout(function () { map.setViewport(bdpolygon.getPath()); }, 100);
}

/**
* 绑定右键菜单
*/
ExpLink.ExpdopDrawRegionManager.prototype._bindContextMenu = function (polygon)
{
    var regionManager = this;
    var contextMenu = new BMap.ContextMenu();
    contextMenu.addItem(new BMap.MenuItem('编辑', function (e, ee, overlay)
    {
        if (regionManager._currentPolygon != null)
        {
            regionManager._dispatchWarning("系统处于编辑状态，请先保存后再试");
            return;
        }
        regionManager._currentPolygon = overlay;   //记录当前的编辑对象
        overlay.enableEditing();                   // 开始编辑

        // 修改多边形风格
        regionManager._resetRenderStyles(regionManager._currentPolygon, regionManager._currentPolygon.selfStyleOptions.styleOptions);

        // 将当前编辑的多边形的风格传回
        this.dispatch(new ExpLink.Event(ExpLink.ExpdopDrawRegionManager.EventType.EDITING, regionManager._currentPolygon.selfStyleOptions.styleOptions));
    }));
    polygon.addContextMenu(contextMenu);

    // 右键菜单也是需要改变风格的
    // 修改风格的代码暂时缺失
}

/**
* 绑定覆盖物的点击事件
*/
ExpLink.ExpdopDrawRegionManager.prototype._bindClickEvent = function (overlay)
{
    var drawRegionManager = this;
    overlay.addEventListener("click", function (e)
    {
        // 选中的是当前编辑的对象
        if (drawRegionManager._currentPolygon == overlay)
        {
            return;
        }
        if (drawRegionManager._currentPolygon != null)
        {
            // 当前正在编辑其他对象
            drawRegionManager._dispatchWarning("系统处于编辑状态，无法选中其他对象");
            return;
        }
        var selectedPolygon = e.target;
        if (drawRegionManager._currentSelectedPolygon != null)
        {
            // 还原状态
            var oriStyle = drawRegionManager._currentSelectedPolygon.selfStyleOptions.styleOptions;
            drawRegionManager._resetRenderStyles(drawRegionManager._currentSelectedPolygon, oriStyle);

        }
        // 修改多边形选中时的风格
        var selectedStyle = drawRegionManager._getStyleOptions();
        drawRegionManager._resetRenderStyles(selectedPolygon, selectedStyle);

        // 记录新的当前选中对象
        drawRegionManager._currentSelectedPolygon = selectedPolygon;
    });

}
/**
* 重置覆盖物的渲染状态
*/
ExpLink.ExpdopDrawRegionManager.prototype._resetRenderStyles = function (overlay, styleOptions)
{
    if (!ExpLink.Utility.isDefAndNotNull || !ExpLink.Utility.isDefAndNotNull(styleOptions))
    {
        return;
    }
    //重置状态
    overlay.setFillColor(styleOptions.fillColor);
    overlay.setFillOpacity(styleOptions.fillOpacity);
    overlay.setStrokeColor(styleOptions.strokeColor);
    overlay.setStrokeOpacity(styleOptions.strokeOpacity);
    overlay.setStrokeStyle(styleOptions.strokeStyle);
    overlay.setStrokeWeight(styleOptions.strokeWeight);
}

/**
* 根据多边形参数创建一个新的多边形
* 有些地方还有待重构
*/
ExpLink.ExpdopDrawRegionManager.prototype._createPolygonFromParams = function (overlay)
{
    if (!ExpLink.Utility.isDefAndNotNull(overlay))
    {
        return;
    }

    if (ExpLink.Utility.isString(overlay))
    {
        var jsonResult = ExpLink.Utility.parse(overlay);
        if (!jsonResult.status)
        {
            return;
        }
        overlay = jsonResult.result;
    }

    // 解析地址坐标
    var bdPoints = [];
    for (var i = 0, length = overlay.path.length; i < length; i++)
    {
        var oriPoint = overlay.path[i];
        bdPoints.push(new BMap.Point(oriPoint.lng, oriPoint.lat));
    }

    // 创建多边形，
    var bdpolygon = new BMap.Polygon(bdPoints, overlay.styleOptions);

    // 给多边形覆盖物添加一个自定义的属性
    bdpolygon.selfStyleOptions = overlay;

    return bdpolygon;

}


/**
* 获取当前默认的多边形绘制风格
* 绘制以及选中时的风格
*/
ExpLink.ExpdopDrawRegionManager.prototype._getStyleOptions = function ()
{
    return {
        strokeColor: '#00f',    //边线颜色。
        fillColor: "#000",      //填充颜色。当参数为空时，圆形将没有填充效果。
        strokeWeight: 3,       //边线的宽度，以像素为单位。
        strokeOpacity: 0.6,	   //边线透明度，取值范围0 - 1。
        fillOpacity: 0.2,      //填充的透明度，取值范围0 - 1。
        strokeStyle: 'dashed'   //边线的样式，solid或dashed。
    };
}

/**
* 获取多边形默认的显示效果
* 如果多边形没有指定显示效果，那么用此效果
*/
ExpLink.ExpdopDrawRegionManager.prototype._getExistsOptions = function ()
{
    return {
        strokeColor: '#00f',    //边线颜色。
        fillColor: "#000",      //填充颜色。当参数为空时，圆形将没有填充效果。
        strokeWeight: 3,       //边线的宽度，以像素为单位。
        strokeOpacity: 0.6,	   //边线透明度，取值范围0 - 1。
        fillOpacity: 0.1,      //填充的透明度，取值范围0 - 1。
        strokeStyle: 'solid'   //边线的样式，solid或dashed。
    };
}


/////////////////////////////    店面配置
////////////////////////////     通过搜索或者是点选的方式进行店面配置


/**
* 店面选址事件类型
*/
ExpLink.ShopManagerEventType = {
    TARGETSELECTED: 'targetselected',
    NEWPICK: 'newpick'
};

/**
* 基础设置里边进行店面的配置
* 
* 这个类稍微改下名字就是一个选址交互操作的实现
* 这个功能需要包含编辑功能么？
*/
ExpLink.ExpdopShopManager = function (opts)
{
    ExpLink.Utility.base(this, arguments);

    this._map = opts.map ? opts.map : null;                                // _map 不可为空
    this._searchResultMarkers = [];                                       // 搜索结果marker集合
    this._pickMarker = null;                                              // 当前点选的店面marker
    this._localSearch = null;
    this._mapClickHandler = null;
    this._mapDragedHandler = null;               // 地图拖动结束
    this._currentKeyword = null;


}
ExpLink.Utility.inherits(ExpLink.ExpdopShopManager, ExpLink.Observable);


ExpLink.ExpdopShopManager.prototype.start = function (opts)
{
    if (!ExpLink.Utility.isDefAndNotNull(opts))
    {
        return;
    }
    var enablepick = ExpLink.Utility.isDefAndNotNull(opts.enablepick) ? opts.enablepick : true;
    var enablesearch = ExpLink.Utility.isDefAndNotNull(opts.enablesearch) ? opts.enablesearch : true;

    this.stop();

    if (enablepick)
    {
        this.enablePick();
    }

    if (enablesearch)
    {
        this.enableSearch();
    }

    if (ExpLink.Utility.isDefAndNotNull(opts.editshop))
    {
        this._editShop(opts.editshop);
    }

}


/**
* 启用店面在线搜索方式
*/
ExpLink.ExpdopShopManager.prototype.enableSearch = function ()
{
    // 初始化LocalSearch接口
    if (!ExpLink.Utility.isDefAndNotNull(this._localSearch))
    {
        this._localSearch = new BMap.LocalSearch(this._map, { renderOptions: {}, onSearchComplete: this._searchComplated.bind(this) });
    }
    if (this._mapDragedHandler == null)
    {
        this._mapDragedHandler = this._mapDraged.bind(this);   //  
        this._map.addEventListener('dragend', this._mapDragedHandler); //地图拖动事件
        this._map.addEventListener('zoomend', this._mapDragedHandler);
    }

    return this;
};

/**
* 地图拖拽停止时触发
*/
ExpLink.ExpdopShopManager.prototype._mapDraged = function ()
{
    this.search(this._currentKeyword);
}
/**
* 检索模式： 检索完成
*/
ExpLink.ExpdopShopManager.prototype._searchComplated = function (result)
{
    // 检索失败
    if (this._localSearch.getStatus() != BMAP_STATUS_SUCCESS)
    {
        return;
    }

    // 清除之前的检索结果
    this._clearSearchMarkers();

    // 添加新的搜索结果
    for (var i = 0, length = result.getCurrentNumPois() ; i < length; i++)
    {
        var point = result.getPoi(i);
        this._addShopMarker(point);
    }
}

/**
* 添加一个Marker对象到地图中。
*/
ExpLink.ExpdopShopManager.prototype._addShopMarker = function (point)
{
    // 构建标签
    var marker = new BMap.Marker(point.point);
    // 添加一个名称显示
    var label = new BMap.Label(point.title, { offset: new BMap.Size(20, -10) });
    marker.setLabel(label);
    // 初始化标签点击时显示的弹出窗
    var infoWindow = new BMap.InfoWindow("<div style='line-height:1.8em;font-size:12px;'><b>名称:</b>" + point.title + "</br><b>地址:</b>" + point.address + "</br><b>电话:</b>" + point.phoneNumber + "</br><a style='text-decoration:none;color:#2679BA;float:right' target='_blank' href='" + point.detailUrl + "'>详情>></a></div>");  // 创建信息窗口对象
    var shopManager = this;
    // 检索结果标签的点击事件
    marker.addEventListener("click", function (e)
    {
        marker.openInfoWindow(infoWindow);
        shopManager._setBlueIcon(marker);
        shopManager.dispatch(new ExpLink.Event(ExpLink.ShopManagerEventType.TARGETSELECTED, { position: point.point, address: point.address }));
    });

    // 允许marker拖拽并添加拖拽结束的事件
    marker.enableDragging();
    marker.addEventListener("dragend", function (e)
    {
        shopManager._setBlueIcon(marker);
        // 广播：和选中一样的事件，他们相当于选中一个新的地址 (位置进行了更新，但是地址未更新)
        shopManager.dispatch(new ExpLink.Event(ExpLink.ShopManagerEventType.TARGETSELECTED, { position: e.point, address: point.address }));
    });

    // 添加到地图中显示
    this._map.addOverlay(marker);
    this._searchResultMarkers.push(marker);
}

/**
* 禁用店面在线搜索方式
*/
ExpLink.ExpdopShopManager.prototype.disableSearch = function ()
{
    // 清空历史数据
    this._clearSearchMarkers();

    // 重置搜索引擎
    this._localSearch = null;
    // 将地图拖拽事件释放
    if (this._mapDragedHandler != null)
    {
        this._map.removeEventListener("dragend", this._mapDragedHandler);
        this._map.removeEventListener("zoomend", this._mapDragedHandler);
        this._mapDragedHandler = null;
    }

    this._currentKeyword = "";


    return this;
};

/**
* 清空现有检索结果
*/
ExpLink.ExpdopShopManager.prototype._clearSearchMarkers = function ()
{
    if (!ExpLink.Utility.isArray(this._searchResultMarkers) || this._searchResultMarkers.length == 0)
    {
        return;
    }

    for (var i = 0, length = this._searchResultMarkers.length; i < length; i++)
    {
        this._map.removeOverlay(this._searchResultMarkers[i]);
    }

    this._searchResultMarkers = [];

    return this;
}

/**
* 依据关键字进行目标店面的检索
*/
ExpLink.ExpdopShopManager.prototype.search = function (keyword)
{
    if (!this._localSearch || !keyword)
    {
        return;
    }

    this._currentKeyword = keyword;            // 记录当前关键字

    this._localSearch.searchInBounds(keyword, this._map.getBounds());

    return this;
}

/**
* 启用店面拾取方式
*/
ExpLink.ExpdopShopManager.prototype.enablePick = function ()
{

    // 添加地图对单击事件的响应
    if (this._mapClickHandler == null)
    {
        this._mapClickHandler = this._mapClick.bind(this);   // IE8及以下版本不支持原生的bind方法。
        this._map.addEventListener("click", this._mapClickHandler);
    }

    return this;
};
/**
* 禁用店面拾取方式
*/
ExpLink.ExpdopShopManager.prototype.disablePick = function ()
{
    // 清除地图中单击添加的覆盖标识。
    if (ExpLink.Utility.isDefAndNotNull(this._pickMarker))  // 当前存在选中的marker
    {
        this._map.removeOverlay(this._pickMarker);
        this._pickMarker = null;
    }

    // 移除地图对单击事件的响应
    if (this._mapClickHandler != null)
    {
        this._map.removeEventListener("click", this._mapClickHandler);
        this._mapClickHandler = null;
    }

    return this;
};

/**
* 完全停止店面选址的交互操作
*/
ExpLink.ExpdopShopManager.prototype.stop = function ()
{
    this.disablePick().disableSearch();
};

/**
* 处理地图的鼠标单击事件
*
*/
ExpLink.ExpdopShopManager.prototype._mapClick = function (e)
{

    if (e.overlay != null)        // 先点击的覆盖物
    {
        return;
    }

    if (ExpLink.Utility.isDefAndNotNull(this._pickMarker))
    {
        // 更新marker位置
        this._pickMarker.setPosition(e.point);
    }
    else
    {
        // 新添加一个marker，此marker没有任何信息，且不需要拖动
        this._pickMarker = new BMap.Marker(e.point);
        this._map.addOverlay(this._pickMarker);
    }

    this._setBlueIcon(this._pickMarker);
    this.dispatch(new ExpLink.Event(ExpLink.ShopManagerEventType.NEWPICK, { lng: e.point.lng, lat: e.point.lat }));
}

/**
* 编辑状态
* 初始化店：位置、事件等。
*/
ExpLink.ExpdopShopManager.prototype._editShop = function (opts)
{
    if (!ExpLink.Utility.isDefAndNotNull(opts))
    {
        return;
    }
    // 获取经纬度
    var lng = ExpLink.Utility.isDefAndNotNull(opts.lng) ? opts.lng : 0;
    var lat = ExpLink.Utility.isDefAndNotNull(opts.lat) ? opts.lat : 0;
    //var name = ExpLink.Utility.isDefAndNotNull(opts.name) ? opts.name : "店1";
    // 构建标签
    this._pickMarker = new BMap.Marker(new BMap.Point(lng, lat));
    var shopManager = this;
    // 允许marker拖拽并添加拖拽结束的事件
    this._pickMarker.enableDragging();
    this._pickMarker.addEventListener("dragend", function (e)
    {
        shopManager._setBlueIcon(shopManager._pickMarker);
        // 广播：和选中一样的事件，生成一个新的地址
        shopManager.dispatch(new ExpLink.Event(ExpLink.ShopManagerEventType.NEWPICK, { lng: e.point.lng, lat: e.point.lat }));
    });

    // 将标签添加到地图中
    this._map.addOverlay(this._pickMarker);
    shopManager._setBlueIcon(this._pickMarker);
    // 将地图视域定位到此位置
    var map = this._map;
    setTimeout(function ()
    {
        map.centerAndZoom(new BMap.Point(lng, lat), 16);
    }, 300);

}

/**
* 当前要素icon设置成蓝色
*/
ExpLink.ExpdopShopManager.prototype._setBlueIcon = function (marker)
{
    if (!ExpLink.Utility.isDefAndNotNull(marker))
    {
        return;
    }

    var blueMarkerIcon = new BMap.Icon("../Images/blue.png", new BMap.Size(22, 25), { anchor: new BMap.Size(10, 25) });
    var redMarkerIcon = new BMap.Icon("../Images/red.png", new BMap.Size(22, 25), { anchor: new BMap.Size(10, 25) });
    // 低效，但是省劲
    for (var i = 0, length = this._searchResultMarkers.length; i < length; i++)
    {
        this._searchResultMarkers[i].setIcon(redMarkerIcon);
    }
    if (ExpLink.Utility.isDefAndNotNull(this._pickMarker))
    {
        this._pickMarker.setIcon(redMarkerIcon);
    }

    marker.setIcon(blueMarkerIcon);
}



/////////////////////////////////
////////////////////////////////


//////////////////////////////////
//应用于O2O业务的标注管理。  --订单监控


/**
* 标注管理中涉及的事件类型
*/
ExpLink.ExpdopMarkerEventType = {
    SHOPCLICK: "shopclick",
    COURIERCLICK: "courierclick",
    MENUCLICK: "menuclick"
};



/**
* 应用于O2O业务的标注管理。
* 处理店、配送范围、配送员、订单的可视化显示以及交互操作
* 继承自 ExpLink.Observable
*/
ExpLink.ExpdopMarkerManager = function (opts)
{
    this._map = opts.map ? opts.map : null;
    this._shopMarkers = [];
    this._courierMarkers = [];
    this._businessMarkers = [];
    this._orderMarkers = [];
    //调用基类构造
    ExpLink.Utility.base(this, opts);
};
ExpLink.Utility.inherits(ExpLink.ExpdopMarkerManager, ExpLink.Observable);


/**
* 定位到指定坐标区域
*/
ExpLink.ExpdopMarkerManager.prototype.FlyTo = function (coordinate)
{

    if (!ExpLink.Utility.isDefAndNotNull(coordinate))
    {
        return;
    }
    // 坐标转换

    var coordsResult = ExpLink.Utility.parse(coordinate);
    if (!coordsResult.status)
    {
        return;
    }
    var coords = coordsResult.result;
    var bdPoints = [];
    for (var i = 0, length = coords.path.length; i < length; i++)
    {
        var tempPoint = coords.path[i];
        bdPoints.push(new BMap.Point(tempPoint.lng, tempPoint.lat));
    }

    // 定位到区域
    var map = this._map;
    setTimeout(function () { map.setViewport(bdPoints); }, 200);

};

/**
* 获取商店信息
* shopInfos ： 店面信息
*/
ExpLink.ExpdopMarkerManager.prototype.setShopInformations = function (shopInfos)
{
    // 通过ajax获取商店信息（如果在此处添加了ajax请求，那么就和业务连到一起了。）
    // 应该说 这一块本来就是业务

    // 信息规范化验证
    if (!ExpLink.Utility.isDefAndNotNull(shopInfos) || !ExpLink.Utility.isArray(shopInfos))
    {
        // 数据检查不合格
        return;
    }
    // 删除现存在的店面信息
    // 先全部删除(实际应进行增量更新)。
    if (ExpLink.Utility.isDefAndNotNull(this._shopMarkers))
    {
        // 删除地图中marker
        for (var i = 0, length = this._shopMarkers.length; i < length; i++)
        {
            this._map.removeOverlay(this._shopMarkers[i]);
        }
        // 清空列表
        this._shopMarkers = [];
    }

    // 添加新的店面信息显示
    // 显示商店信息
    var markerManager = this;
    for (var i = 0, length = shopInfos.length; i < length; i++)
    {
        (function ()
        {
            // 构造marker
            var shop1 = shopInfos[i];
            var shop1Position = ExpLink.Utility.parse(shop1.stoCoordinate);
            if (!shop1Position.status)
            {
                return;
            }
            var shopmarker1 = new BMap.Marker(new BMap.Point(shop1Position.result.lng, shop1Position.result.lat));
            shopmarker1.shopContent = shop1.id;
            // 添加文字标注
            var label = new BMap.Label(shop1.stoName, { offset: new BMap.Size(20, -10) });
            shopmarker1.setLabel(label);
            // 记录当前地图对象
            shop1.map = markerManager._map;
            // 绑定click事件
            shopmarker1.addEventListener("click", function ()
            {
                markerManager.dispatch(new ExpLink.Event(ExpLink.ExpdopMarkerEventType.SHOPCLICK, shop1));
            });
            // 添加到地图和列表中
            markerManager._shopMarkers.push(shopmarker1);
            markerManager._map.addOverlay(shopmarker1);
        })();
    }
}

/**
* 指定ID店面运行动画效果
*
**/
ExpLink.ExpdopMarkerManager.prototype.setShopAnimation = function (id)
{
    if (!ExpLink.Utility.isArray(this._shopMarkers) || this._shopMarkers.length == 0)
    {
        return this;
    }
    var markerManager = this;
    for (var i = 0, length = markerManager._shopMarkers.length; i < length; i++)
    {
        if (markerManager._shopMarkers[i].shopContent == id)
        {
            (function ()
            {
                // 显示店面
                markerManager._shopMarkers[i].show();
                // 设置动画，2s后结束
                markerManager._shopMarkers[i].setAnimation(BMAP_ANIMATION_BOUNCE);
                setTimeout(function ()
                {
                    markerManager._shopMarkers[i].setAnimation(null);
                }, 2000);
            })();
        }
        else
        {
            // 隐藏其他非相关店面
            markerManager._shopMarkers[i].hide();
        }
    }
    return this;
}

/**
* 设置店面的可见性
*
***/
ExpLink.ExpdopMarkerManager.prototype.setShopVisible = function (visible)
{
    if (!ExpLink.Utility.isArray(this._shopMarkers) || this._shopMarkers.length == 0)
    {
        return;
    }
    if (visible)
    {
        for (var i = 0, length = this._shopMarkers.length; i < length; i++)
        {
            this._shopMarkers[i].show();
        }
    }
    else
    {
        for (var i = 0, length = this._shopMarkers.length; i < length; i++)
        {
            this._shopMarkers[i].hide();
        }
    }
    return this;
};

/**
* 获取配送员信息
*    
*/
ExpLink.ExpdopMarkerManager.prototype.setCourierInformations = function (couriers)
{
    // 通过ajax请求数据

    // 数据规范化检查
    if (!ExpLink.Utility.isArray(couriers))
    {
        // 数据检查未通过
        return;
    }
    // 5分钟没更新信息则自我删除(增量更新的时候可以考虑下) ： 在服务器端实现
    // 感觉配送员和订单雷同

    // 差量检测
    // 对订单进行差异分析
    var existsCouriers = ExpLink.algo.interdiff(this._courierMarkers, function (ele)
    {
        return ele.courierContext;
    },
    couriers, function (ele)
    {
        return ele.id;
    });

    // 删除不应该存在的配送员
    this._clearCouriers(existsCouriers.diff);

    // 对现有配送员进行更新
    this._resetCourierMarker(existsCouriers.inter, couriers);

    // 添加新的配送员信息
    // 计算新的配送员
    var newCouriers = ExpLink.algo.linq(couriers).difference(this._courierMarkers, function (ele)
    {
        return ele.id;
    }, function (ele)
    {
        return ele.courierContext;
    }).toArray();

    // 显示配送员信息
    var markerManager = this;
    for (var i = 0, length = newCouriers.length; i < length; i++)
    {
        (function ()
        {
            // 构建marker
            var courier = newCouriers[i];
            var couriermarker = this._getCourierMarker(courier);

            if (couriermarker == null)
            {
                return;
            }

            // 记录ID
            couriermarker.courierContext = courier.id;

            // 绑定单击事件
            couriermarker.addEventListener("click", function ()
            {
                // 为了显示infowindow，此处需要记录部分关键对象的引用
                courier.map = markerManager._map;
                // 广播事件
                markerManager.dispatch(new ExpLink.Event(ExpLink.ExpdopMarkerEventType.COURIERCLICK, courier));
            });

            // 添加到地图以及列表中
            this._map.addOverlay(couriermarker);
            this._courierMarkers.push(couriermarker);

            //// 添加一个测试用的marker，用来调整自定义marker的位置
            //var pmarker = new BMap.Marker(new BMap.Point(courier.lng, courier.lat));
            //this._map.addOverlay(pmarker);
        }).call(this, i);    //绑定作用域
    }
}

/**
* 更新配送员marker状态
* 增量更新时会用到
*/
ExpLink.ExpdopMarkerManager.prototype._resetCourierMarker = function (courierMarkers, courierInfos)
{
    if (!ExpLink.Utility.isArray(courierMarkers) || !ExpLink.Utility.isArray(courierInfos))
    {
        return;
    }

    var dic = {};     // 记录

    for (var i = 0, length = courierInfos.length; i < length; i++)
    {
        dic[courierInfos[i].id];
    }

    for (var i = 0, length = courierMarkers.length; i < length; i++)
    {
        var courierInfo = dic[courierMarkers[i].courierContext];
        if (!ExpLink.Utility.isDefAndNotNull(courierInfo))
        {
            continue;
        }
        // 更新显示内容
        var content = this._getCourierContent(courierInfo);
        courierMarkers[i].setContent(content);
        // 更新位置

        var positionResult = ExpLink.Utility.parse(courierInfo.coordinate);
        if (!positionResult.status)
        {
            return;
        }
        var position = positionResult.result;
        courierMarkers[i].setPosition(new BMap.Point(position.lng, position.lat));
    }

};

/**
* 构造表示配送员的marker
*/
ExpLink.ExpdopMarkerManager.prototype._getCourierMarker = function (courierInfo)
{
    /*    采用复杂内容的marker
    //var content = this._getCourierContent(courierInfo);
    //var position = JSON.parse(courierInfo.coordinate);
    //var couriermarker = new BMapLib.RichMarker(content.content, new BMap.Point(position.lng, position.lat), {
    //    "anchor": new BMap.Size(content.x, content.y)
    //});
    //return couriermarker;
    */
    // 更换图标
    var imgName = "del" + courierInfo.status + ".png";
    var markerIcon = new BMap.Icon("~/Images/" + imgName, new BMap.Size(22, 30));
    // 添加文字标签
    var markerLabel = new BMap.Label(courierInfo.delName, { offset: new BMap.Size(20, -10) });

    var position = ExpLink.Utility.parse(courierInfo.coordinate);
    if (!position.status)
    {
        return null;
    }
    // 构建marker
    var courierMarker = new BMap.Marker(new BMap.Point(position.result.lng, position.result.lat));
    courierMarker.setTop(true);
    courierMarker.setIcon(markerIcon);
    courierMarker.setLabel(markerLabel);

    return courierMarker;

}
/**
* 获取配送员展示图标信息
* 
*/
ExpLink.ExpdopMarkerManager.prototype._getCourierContent = function (courierInfo)
{
    var contentstring = '<div style="position: absolute; margin: 0pt; padding: 0pt; width: 80px; height: 26px; left: -18px; top: -25px; overflow: hidden;">'
                 + '<img id="rm3_image" style="border:none;left:0px; top:0px; position:absolute;" src="../../Images/back3.png">'
                 + '</div>'
                 + '<label class=" BMapLabel" unselectable="on" style="position: absolute; -moz-user-select: none; display: inline; cursor: inherit; border: 0px none; padding: 2px 1px 1px; white-space: nowrap; font: 12px arial,simsun; z-index: 80; color: rgb(255, 102, 0); left: 12px; top: -25px;">'
                 + courierInfo.delName
                 + '</label>'

    return { content: contentstring, x: -0, y: -0 };
};

/**
* 删除配件员marker
*/
ExpLink.ExpdopMarkerManager.prototype._clearCouriers = function (opts)
{
    // 如果指定参数，那么删除指定marker，如果不指定参数(参数不合格)，那么删除全部。
    if (ExpLink.Utility.isArray(opts))
    {
        for (var i = 0, length = opts.length; i < length; i++)
        {
            this._map.removeOverlay(opts[i]);

            for (var j = 0, cmarkersLength = this._courierMarkers.length; j < cmarkersLength; j++)
            {
                if (opts[i].courierContext == this._courierMarkers[j].courierContext)
                {
                    this._courierMarkers.splice(j, 1);
                    break;
                }
            }
        }
    }
    else
    {
        for (var i = 0, length = this._courierMarkers.length; i < length; i++)
        {
            this._map.removeOverlay(this._courierMarkers[i]);
        }
        this._courierMarkers = [];
    }
};


/**
* 地图中显示和店关联的配送范围。
* 一次添加多个配送范围的显示。
* 此方法默认会删除地图中现显示的配送范围。
* opts:{regions:[]};
*/
ExpLink.ExpdopMarkerManager.prototype.showBusinessRegions = function (opts)
{
    // 未定义参数
    if (!ExpLink.Utility.isDefAndNotNull(opts))
    {
        return;
    }

    var regions = opts.regions ? opts.regions : [];

    // 传入的区域参数不是数组对象
    if (!ExpLink.Utility.isArray(regions))
    {
        return;
    }

    // 删除已存在的区域对象

    this.clearBusinessRegions();

    //添加区域对象 
    var points = [];
    for (var i = 0, length = regions.length; i < length; i++)
    {
        var polygon = this._getRegion(regions[i]);    // 创建地图中多边形对象。
        this._map.addOverlay(polygon);                // 添加到地图覆盖层显示。
        this._businessMarkers.push(polygon);          // 将多边形保存。

        // 记录多边形顶点
        points = points.concat(polygon.getPath());

    }


    return this;

}

/**
* 删除配送范围的显示
*/
ExpLink.ExpdopMarkerManager.prototype.clearBusinessRegions = function ()
{
    if (ExpLink.Utility.isArray(this._businessMarkers) && this._businessMarkers.length > 0)
    {
        for (var i = 0, length = this._businessMarkers.length; i < length; i++)
        {
            this._map.removeOverlay(this._businessMarkers[i]);
        }
        this._businessMarkers = [];
    }

    return this;
}

/**
* 获取（创建）一个区域对象
* 显示到地图中的对象
*/
ExpLink.ExpdopMarkerManager.prototype._getRegion = function (region)
{
    // 数据规范化检查
    if (!ExpLink.Utility.isDefAndNotNull(region))
    {
        return null;
    }

    // 将json字符串转成json对象
    if (ExpLink.Utility.isString(region))
    {
        var regionResult = ExpLink.Utility.parse(region);
        if (!regionResult.status)
        {
            return null;
        }
        region = regionResult.result;
    }

    // 坐标转换成BMap.Point类型
    var bdPoints = [];
    for (var i = 0, length = region.path.length; i < length; i++)
    {
        var tempPoint = region.path[i];
        bdPoints.push(new BMap.Point(tempPoint.lng, tempPoint.lat));
    }

    // 创建多边形
    var polygon = new BMap.Polygon(bdPoints, region.styleOptions);

    return polygon;
}

/**
* 获取默认的区域渲染风格
*/
/*
ExpLink.ExpdopMarkerManager.prototype._getRegionStyleOptions = function (opts)
{
    return {
        strokeColor: '#00f',    //边线颜色。
        fillColor: "#000",      //填充颜色。当参数为空时，圆形将没有填充效果。
        strokeWeight: 3,       //边线的宽度，以像素为单位。
        strokeOpacity: 0.6,	   //边线透明度，取值范围0 - 1。
        fillOpacity: 0.2,      //填充的透明度，取值范围0 - 1。
        strokeStyle: 'solid'   //边线的样式，solid或dashed。
    };
}
*/

/**
* 地图中显示订单目的地位置
* 一次添加多个订单的显示
* 此方法默认会删除地图中现有的订单显示
* opts:订单的数组？ 
* 返回没有找到地址的订单？
*/
ExpLink.ExpdopMarkerManager.prototype.showOrders = function (opts)
{
    if (!ExpLink.Utility.isDefAndNotNull(opts))  // 没传递参数就返回
    {
        return null;
    }

    var _orders = opts.orders ? opts.orders : [];       // 订单列表

    // 指定区域坐标地址
    var filterRegion = opts.filterregion ? opts.filterregion : null;

    if (!ExpLink.Utility.isArray(_orders))           // 参数是否以数组的形式存在
    {
        return null;
    }

    // 清除现在显示的orders
    //this.clearOrders();

    // 对订单进行差异分析
    var existsOrder = ExpLink.algo.interdiff(this._orderMarkers, function (ele)
    {
        return ele.orderContext;
    },
    _orders, function (ele)
    {
        return ele.id;
    });


    //删掉不存在的订单
    this.clearOrders(existsOrder.diff);

    // 更新现有订单
    this.resetOrderStatus(existsOrder.inter, _orders);        // 此处填写_orders 是不恰当的； 它实际上是一个超集
    // 更新订单marker记录
    this._orderMarkers = existsOrder.inter;

    // 添加新的订单
    // 获取订单增量
    var newOrder = ExpLink.algo.linq(_orders).difference(this._orderMarkers, function (ele)
    {
        return ele.id;
    }, function (ele)
    {
        return ele.orderContext;
    }).toArray();

    var markerManager = this;  // 保存标识管理对象实例
    var failOrders = [];   // 地址匹配失败的订单

    // 添加订单的地图显示
    for (var i = 0, length = newOrder.length; i < length; i++)
    {
        (function ()
        {
            var order = newOrder[i];
            var address = order.custAddress;
            var name = order.custName;
            var telephone = order.telephone;
            var status = order.status;
            var id = order.id;
            var position = ExpLink.Utility.parse(order.custPosition);
            if (position.status)
            {
                var point = new BMap.Point(position.result.lng, position.result.lat);
                //构建订单marker
                var orderMarker = markerManager._getOrderMarker({
                    point: point, custName: name, address: address, teldephone: telephone, status: status
                });
                // 记录ID
                orderMarker.orderContext = id;
                // 添加到地图中
                markerManager._addOrder(orderMarker)        //
            }
            //ExpLink.ExpdopGeoCoder.geoCoder(address, function (point)
            //{
            //    // 地理编码失败 point参数值会为null
            //    if (ExpLink.Utility.isDefAndNotNull(point))   // 只有地址解析出结果的订单才会添加到地图上
            //    {
            //        // 是否按筛选区域进行匹配
            //        if (!ExpLink.Utility.isDefAndNotNull(filterRegion) || markerManager._isInPolygons(point, filterRegion))
            //        {
            //            // 构建订单marker
            //            var orderMarker = markerManager._getOrderMarker({
            //                point: point, custName: name, address: address, teldephone: telephone, status: status
            //            });
            //            // 记录ID
            //            orderMarker.orderContext = id;
            //            // 添加到地图中
            //            markerManager._addOrder(orderMarker)        //
            //        }
            //    }
            //    else
            //    {
            //        // 解析失败？  是返回订单，还是进行关键字筛选查询呢？
            //        failOrders.push(order);
            //    }
            //});
        })();
    }

    return failOrders;   // 返回地址匹配失败的订单
}

/**
* 重置单个订单的显示状态
* 增量更新时会用到
*/
ExpLink.ExpdopMarkerManager.prototype.resetOrderStatus = function (orderMarkers, orderInfos)
{
    // 数据合理性检查
    if (!ExpLink.Utility.isArray(orderMarkers) || !ExpLink.Utility.isArray(orderInfos))
    {
        return;
    }

    // 重置订单标签内容
    var dic = {};
    for (var i = 0, length = orderInfos.length; i < length; i++)
    {
        dic[orderInfos[i].id] = orderInfos[i];
    }

    for (var i = 0, length = orderMarkers.length; i < length; i++)
    {
        var content = this._getOrderContent(dic[orderMarkers[i].orderContext]);
        orderMarkers[i].setContent(content.content);
    }
}


/**
* 根据ID 获取标签在地图中的位置
*/
ExpLink.ExpdopMarkerManager.prototype.getOrderMarkerPosition = function (id)
{
    if (!ExpLink.Utility.isArray(this._orderMarkers))
    {
        return null;
    }

    for (var i = 0, length = this._orderMarkers.length; i < length; i++)
    {
        if (id == this._orderMarkers[i].orderContext)
        {
            var position = this._orderMarkers[i].getPosition();
            return { lng: position.lng, lat: position.lat };
        }
    }
    return null;
}

/**
* 将订单marker添加到地图中显示
*/
ExpLink.ExpdopMarkerManager.prototype._addOrder = function (orderMarker)
{
    if (!ExpLink.Utility.isDefAndNotNull(orderMarker))
    {
        return;
    }

    this._map.addOverlay(orderMarker);         // 添加到地图中显示
    this._orderMarkers.push(orderMarker);      // 添加到默认的保存列表中

    return this;
}

/**
* 判断一个坐标点是否在一个或者多个多边形内
* @point ： BMap.Point类型的点数据
* @polygons: 字符串或字符串数组，
* 其中字符串格式为json格式的顶点数组，顶点必须包含lng和lat属性
*/
ExpLink.ExpdopMarkerManager.prototype._isInPolygons = function (point, polygons)
{
    if (!ExpLink.Utility.isDefAndNotNull(point) || !ExpLink.Utility.isDefAndNotNull(polygons))
    {
        return false;
    }

    if (ExpLink.Utility.isArray(polygons))     // 传入的多个多边形
    {
        polygons.forEach(function (ele, index)
        {
            if (isInPolygon(point, ele))
            {
                return true;                   // 有一返回true 则结束
            }
        });
    }
    else                                      // 传入参数为一个多边形的情况
    {
        return isInPolygon(point, polygons);
    }

    // 点不在任何多边形内
    return false;

    // 判断点是否在多边形内
    function isInPolygon(point, polygon)
    {
        var ptsResult = ExpLink.Utility.parse(polygon);
        if (!ptsResult.status)
        {
            return false;
        }

        var pts = ptsResult.result;
        var bmappts = [];
        pts.forEach(function (ele, index, arr)   // json转回的数据不再是bmap.point对象，需要新建
        {
            bmappts.push(new BMap.Point(ele.lng, ele.lat));
        });
        var bmappolygon = new BMap.Polygon(bmappts);         // 创建BMap.Polygon 对象

        return BMapLib.GeoUtils.isPointInPolygon(point, bmappolygon);
    }

}

/**
* 删除订单在地图上的显示
* 删除全部订单或部分订单
*/
ExpLink.ExpdopMarkerManager.prototype.clearOrders = function (orders)
{

    if (ExpLink.Utility.isArray(orders))
    {
        for (var i = 0, length = orders.length; i < length; i++)
        {
            this._map.removeOverlay(orders[i]);

            for (var j = 0, corderLength = this._orderMarkers.length; j < corderLength; j++)
            {
                if (orders[i].orderContext == this._orderMarkers[j].orderContext)
                {
                    this._orderMarkers.splice(j, 1);
                    break;
                }
            }

        }
    }
    else if (ExpLink.Utility.isArray(this._orderMarkers) && this._orderMarkers.length > 0)
    {
        for (var i = 0, length = this._orderMarkers.length; i < length; i++)
        {
            this._map.removeOverlay(this._orderMarkers[i]);
        }
        this._orderMarkers = [];
    }
};


/**
* 获取(创建一个代表订单的marker对象)
* 参数中需要包含   名称、电话、地址（文字）、位置（经纬度）等信息
*/
ExpLink.ExpdopMarkerManager.prototype._getOrderMarker = function (opts)
{
    if (!ExpLink.Utility.isDefAndNotNull(opts))  // 参数未定义
    {
        return;
    };
    var content = this._getOrderContent(opts);
    var ordermarker = new BMapLib.RichMarker(content.content, new BMap.Point(opts.point.lng, opts.point.lat), {
        "anchor": new BMap.Size(content.x, content.y)
    });
    return ordermarker;

};

/**
* 获取订单的展示图标内容
* 参数中需要包含一些 名称、电话、地址(文字)等等的信息
*/
ExpLink.ExpdopMarkerManager.prototype._getOrderContent = function (opts)
{

    if (!ExpLink.Utility.isDefAndNotNull(opts))
    {
        return { content: "", x: 0, y: 0 };
    }
    var imgName = opts.status + ".png"
    var contentstring = '<div style="position: absolute; margin: 0pt; padding: 0pt; width: 80px; height: 26px; left: 0px; top: 0px; overflow: hidden;">'
                 + '<img id="' + opts.status + '" style="border:none;left:0px; top:0px; position:absolute;" src="../../Images/' + imgName + '">'
                 + '</div>'
                 + '<label class=" BMapLabel" unselectable="on" style="position: absolute; -moz-user-select: none; display: inline; cursor: inherit; border: 0px none; padding: 2px 1px 1px; white-space: nowrap; font: 12px arial,simsun; z-index: 80; color: rgb(255, 102, 0); left: 25px; top: 0px;">'
                 + opts.custName
                 + '</label>'

    return { content: contentstring, x: -18, y: -27 };
};



////////////////////////////////
/// 地理编码和逆地理编码
/**
* 地理编码和逆地理编码实现
* 为实现静态方法。
*/
ExpLink.ExpdopGeoCoder = function ()
{
};

/**
* 地理编码
* 从地址获取目标经纬度信息
*/
ExpLink.ExpdopGeoCoder.geoCoder = function (address, callbackfunction)
{
    var geoCoder = new BMap.Geocoder();
    geoCoder.getPoint(address, callbackfunction);
    return this;
};

/**
* 逆地理编码
* 从经纬度解析出地址。
* lng:经度(小数表示)
* lat:纬度(小数表示)
*/
ExpLink.ExpdopGeoCoder.unGeoCoder = function (lng, lat, callbackfunction)
{
    var point = new BMap.Point(lng, lat);
    return ExpLink.ExpdopGeoCoder.unGeoCoderPt(point, callbackfunction);
};
/**
* 逆地理编码
* 从经纬度解析出地址。
* point type: BMap.Point
*/
ExpLink.ExpdopGeoCoder.unGeoCoderPt = function (point, callbackfunction)
{
    var geoCoder = new BMap.Geocoder();
    geoCoder.getLocation(point, callbackfunction);
    return this;
};


/**
* 基于地理信息的位置服务命名空间
*/
ExpLink.lbs = function ()
{ };

/**
* 基于位置服务的基础功能命名空间
*/
ExpLink.lbs.utility = function ()
{ };
/**
* 计算一组顶点的中心点
* pts: ExpLink.lbs.Point[]
*/
ExpLink.lbs.utility.getCenter = function (pts)
{
    if (!ExpLink.Utility.isDefAndNotNull(pts) || !ExpLink.Utility.isArray(pts) || pts.length == 0)
    {
        return null;
    }
    var cenLng = 0;
    var cenLat = 0;
    var length = pts.length;
    for (var i = 0; i < length; i++)
    {
        cenLng += pts[i].lng;
        cenLat += pts[i].lat;
    }
    cenLng = cenLng / length;
    cenLat = cenLat / length;
    return new ExpLink.lbs.Point(cenLng, cenLat);
};


/**
* 根据传入参数创建新的渲染风格
* 静态方法
* 如果相对应的参数没有传入，那么采用默认风格
*{
*  strokeColor: '#00f',    //边线颜色。
*  fillColor: "#000",      //填充颜色。当参数为空时，圆形将没有填充效果。
*  strokeWeight: 3,       //边线的宽度，以像素为单位。
*  strokeOpacity: 0.6,	   //边线透明度，取值范围0 - 1。
*  fillOpacity: 0.2,      //填充的透明度，取值范围0 - 1。
*  strokeStyle: 'solid'   //边线的样式，solid或dashed。
*}
*/
ExpLink.lbs.utility.createStyleOptions = function (opts)
{
    if (!ExpLink.Utility.isDefAndNotNull(opts))
    {
        // 如果没有填写参数，那么会返回一个空对象
        // 是什么样的渲染效果 ？？？
        return {};
    }

    return {
        strokeColor: opts.strokeColor || '#00f',    //边线颜色。
        fillColor: opts.fillColor || "#fff",      //填充颜色。当参数为空时，圆形将没有填充效果。
        strokeWeight: opts.strokeWeight || 1,       //边线的宽度，以像素为单位。
        strokeOpacity: opts.strokeOpacity || 0.8,	   //边线透明度，取值范围0 - 1。
        fillOpacity: opts.fillOpacity || 0.2,      //填充的透明度，取值范围0 - 1。
        strokeStyle: opts.strokeStyle || 'solid'   //边线的样式，solid或dashed。
    };
};

/**
* 点
*/
ExpLink.lbs.Point = function (lng, lat)
{
    this.lng = lng;
    this.lat = lat;
};

/**
* 矩形
*/
ExpLink.lbs.Rectangle = function (leftTop, rightBottom)
{
    this.leftTop = leftTop;
    this.rightBottom = rightBottom;
};

/**
* 获取一组顶点的外包围矩形
* 静态方法
*/
ExpLink.lbs.Rectangle.fromPoints = function (pts)
{
    //数据检测
    if (!ExpLink.Utility.isDefAndNotNull(pts) || !ExpLink.Utility.isArray(pts) || pts.length == 0)
    {
        return null;
    }
    // 获取一组顶点的外接矩形
    var first = pts[0];  // 记录第一个点
    var maxLng = first.lng;    //最大经度
    var maxLat = first.lat;    //最大纬度
    var minLng = first.lng;	//最小经度
    var minLat = first.lat;	//最小纬度
    // 遍历每个点
    for (var i = 1, length = pts.length; i < length; i++)
    {
        // 最大经度
        if (pts[i].lng > maxLng)
        {
            maxLng = pts[i].lng;
        }
        // 最小经度
        if (pts[i].lng < minLng)
        {
            minLng = pts[i].lng;
        }
        // 最大纬度
        if (pts[i].lat > maxLat)
        {
            maxLat = pts[i].lat;
        }
        // 最小纬度
        if (pts[i].lat < minLat)
        {
            minLat = pts[i].lat;
        }
    }
    //  构造矩形
    var leftTop = new ExpLink.lbs.Point(minLng, maxLat);
    var rightBottom = new ExpLink.lbs.Point(maxLng, minLat);
    return new ExpLink.lbs.Rectangle(leftTop, rightBottom);
};

/**
* 多边形
* pts: 顶点数组
* id:ID
* style: 渲染风格
*/
ExpLink.lbs.Polygon = function (pts, id, style)
{

    this.id = ExpLink.Utility.guid();   // 默认使用创建的guid

    // 渲染风格
    this.styleOptions = style || ExpLink.lbs.utility.createStyleOptions({});   // 创建默认的渲染风格
    // 多边形顶点
    this.path = pts;
    // 外接矩形
    this.extent = ExpLink.lbs.Rectangle.fromPoints(pts);
    // 中心点 
    this.center = ExpLink.lbs.utility.getCenter(pts);
};









/**
* 站点分单
*/



ExpLink.DeliveryStationEventType = {
    STATIONMARKERCLICK: "stationmarkerclick",
    STATIONREGIONCLICK: "stationregionclick"
};

/**
*   配送站
*   1.显示站点和区域。
*   2.点击站点和区域时会触发事件。
*/
ExpLink.DeliveryStation = function (opts)
{
    if (!ExpLink.Utility.isDefAndNotNull(opts) || !ExpLink.Utility.isDefAndNotNull(opts.map))
    {
        // 未指定地图，返回
        return;
    }

    this._map = opts.map;
    this._stationMarkers = [];
    this._regionMarkers = [];
    this._currentSelectedRegionMarker = null;


    //调用基类构造
    ExpLink.Utility.base(this, opts);

};
ExpLink.Utility.inherits(ExpLink.DeliveryStation, ExpLink.Observable);


ExpLink.DeliveryStation.prototype.setDeliveryStationItems = function (delStations)
{
    if (!ExpLink.Utility.isArray(delStations))
    {
        return;
    }

    // 清除已有

    this.clearAllOverlay();

    // 添加
    for (var i = 0, length = delStations.length; i < length; i++)
    {
        this._addStationRegion(delStations[i]);
    }

};

/**
* 获取当前选中站点的ID
**/
ExpLink.DeliveryStation.prototype.getSelectedDeliveryStationID = function ()
{
    if (this._currentSelectedRegionMarker != null)
    {
        return this._currentSelectedRegionMarker.delStat.id;
    }
};



/**
* 添加一个站点区域
*/
ExpLink.DeliveryStation.prototype._addStationRegion = function (delstat)
{
    if (!ExpLink.Utility.isDefAndNotNull(delstat))
    {
        return;
    }
    var polygonCoordinateStr = delstat.coordinate;

    if (!ExpLink.Utility.isDefAndNotNull(polygonCoordinateStr))
    {
        return;
    }

    // 创建多边形，
    var bdpolygon = this._createPolygonFromParams(polygonCoordinateStr, delstat);

    if (!ExpLink.Utility.isDefAndNotNull(bdpolygon))
    {
        return;
    }
    // 记录站点全部信息
    bdpolygon.delStat = delstat;

    // 绑定点击事件
    this._bindClickEvent(bdpolygon, delstat);

    // 添加到地图中
    this._regionMarkers.push(bdpolygon);
    this._map.addOverlay(bdpolygon);

    // 在中心点添加一个marker (外包围框的中心点，可能并不在多边形内)
    var center = bdpolygon.getBounds().getCenter();
    this._addStationMarker(center, delstat);

};


/**
* 添加一个站点marker
**/
ExpLink.DeliveryStation.prototype._addStationMarker = function (point, delstat)
{
    // 构建标签
    var marker = new BMap.Marker(point);
    // 添加一个名称显示
    var label = new BMap.Label(delstat.name, { offset: new BMap.Size(20, 0) });
    marker.setLabel(label);
    // 初始化标签点击时显示的弹出窗
    //var infoWindow = new BMap.InfoWindow("<div style='line-height:1.8em;font-size:12px;'><b>名称:</b>" + point.title + "</br><b>地址:</b>" + point.address + "</br><b>电话:</b>" + point.phoneNumber + "</br><a style='text-decoration:none;color:#2679BA;float:right' target='_blank' href='" + point.detailUrl + "'>详情>></a></div>");  // 创建信息窗口对象
    //var shopManager = this;
    // 检索结果标签的点击事件
    //marker.addEventListener("click", function (e)
    //{
    //    marker.openInfoWindow(infoWindow);
    //    shopManager._setBlueIcon(marker);
    //    shopManager.dispatch(new ExpLink.Event(ExpLink.ShopManagerEventType.TARGETSELECTED, { position: point.point, address: point.address }));
    //});

    // 添加到地图中
    this._map.addOverlay(marker);
    this._stationMarkers.push(marker);

};


/**
* 绑定点击事件
**/
ExpLink.DeliveryStation.prototype._bindClickEvent = function (overlay, delstat)
{
    var _this = this;
    overlay.addEventListener("click", function (e)
    {
        // 选中的是当前编辑的对象
        if (_this._currentSelectedRegionMarker == overlay)
        {
            return;
        }

        var selectedMarker = e.target;
        if (_this._currentSelectedRegionMarker != null)
        {
            // 还原状态
            var oriStyle = _this._currentSelectedRegionMarker.selfStyleOptions.styleOptions;
            _this._resetRenderStyles(_this._currentSelectedRegionMarker, oriStyle);

        }
        // 修改多边形选中时的风格
        var selectedStyle = _this._getSelectedStyles();
        _this._resetRenderStyles(selectedMarker, selectedStyle);

        // 记录新的当前选中对象
        _this._currentSelectedRegionMarker = selectedMarker;

        // 分发选中事件
        _this.dispatch(new ExpLink.Event(ExpLink.DeliveryStationEventType.STATIONREGIONCLICK, delstat.id));

    });
};

/**
* 清除区域和站点
*/
ExpLink.DeliveryStation.prototype.clearAllOverlay = function ()
{
    if (ExpLink.Utility.isDefAndNotNull(this._map))
    {
        return;
    }

    // 站标
    if (ExpLink.Utility.isArray(this._stationMarkers))
    {
        for (var i = 0, length = this._stationMarkers.length; i < length; i++)
        {
            this._map.removeOverlay(this._stationMarkers[i]);
        }
        this._stationMarkers = [];
    }

    // 区域
    if (ExpLink.Utility.isArray(this._regionMarkers))
    {
        for (var i = 0, length = this._regionMarkers.length; i < length; i++)
        {
            this._map.removeOverlay(this._regionMarkers[i]);
        }
        this._regionMarkers = [];
    }

    // 当前选中置空
    this._currentSelectedRegionMarker = null;

};


/**
* 重置覆盖物的渲染状态
*/
ExpLink.DeliveryStation.prototype._resetRenderStyles = function (overlay, styleOptions)
{
    if (!ExpLink.Utility.isDefAndNotNull || !ExpLink.Utility.isDefAndNotNull(styleOptions))
    {
        return;
    }
    //重置状态
    overlay.setFillColor(styleOptions.fillColor);
    overlay.setFillOpacity(styleOptions.fillOpacity);
    overlay.setStrokeColor(styleOptions.strokeColor);
    overlay.setStrokeOpacity(styleOptions.strokeOpacity);
    overlay.setStrokeStyle(styleOptions.strokeStyle);
    overlay.setStrokeWeight(styleOptions.strokeWeight);
}

/**
* 根据多边形参数创建一个新的多边形
* 有些地方还有待重构
*/
ExpLink.DeliveryStation.prototype._createPolygonFromParams = function (overlay)
{
    if (!ExpLink.Utility.isDefAndNotNull(overlay))
    {
        return;
    }

    if (ExpLink.Utility.isString(overlay))
    {
        var jsonResult = ExpLink.Utility.parse(overlay);
        if (!jsonResult.status)
        {
            return;
        }
        overlay = jsonResult.result;
    }

    // 解析地址坐标
    var bdPoints = [];
    for (var i = 0, length = overlay.path.length; i < length; i++)
    {
        var oriPoint = overlay.path[i];
        bdPoints.push(new BMap.Point(oriPoint.lng, oriPoint.lat));
    }

    // 创建多边形，
    var bdpolygon = new BMap.Polygon(bdPoints, overlay.styleOptions);

    // 给多边形覆盖物添加一个自定义的属性
    bdpolygon.selfStyleOptions = overlay;

    return bdpolygon;

}



/**
* 获取默认的选中区域的风格
*
**/
ExpLink.DeliveryStation.prototype._getSelectedStyles = function ()
{
    return {                    // 这个大括号必须放到这
        strokeColor: '#f00',    //边线颜色。
        fillColor: "#000",      //填充颜色。当参数为空时，圆形将没有填充效果。
        strokeWeight: 5,       //边线的宽度，以像素为单位。
        strokeOpacity: 0.6,	   //边线透明度，取值范围0 - 1。
        fillOpacity: 0.2,      //填充的透明度，取值范围0 - 1。
        strokeStyle: 'solid'   //边线的样式，solid或dashed。
    };
};


























