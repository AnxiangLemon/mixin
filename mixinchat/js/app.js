window.app = {

	/**
	 * netty服务后端发布的url地址
	 */
	nettyServerUrl: 'ws://172.16.10.106:7088/ws',

	/**
	 * 后端服务发布的url地址
	 */
	serverUrl: 'http://172.16.10.106:7080/mixin/',

	/**
	 * 图片服务器的url地址
	 */
	imgServerUrl: 'http://103.229.147.196/group1/',

	/**
	 * 取文件后缀名
	 */
	getFileHouZhuiName: function(filename){
		var index1=filename.lastIndexOf(".");	
		var index2=filename.length;
		var postf=filename.substring(index1+1,index2);//后缀名
//		console.log(postf);
		
		return postf;
		
	},
	/**
	 *替换所有换行符
	 */
	TransferString: function(content) {
		var string = content;
		try {
			string = string.replace(/\r\n/g, "<br/>")
			string = string.replace(/\n/g, "<br/>");
			string = string.replace(/ /g, "&nbsp;");

		} catch (e) {
			console.log(e.message);
		}
		return string;
	},
	/**
	 *替换特使字符
	 */
	ThTsStr: function(content) {
		var string = content;
		try {

			string = string.replace(/>/g, "&gt;")
			string = string.replace(/</g, "&lt;");
			string = string.replace(/\"/g, "&quot;")
			string = string.replace(/\'/g, "&#39;");
			string = string.replace(/ /g, "&nbsp;");
			string = string.replace(/\r\n/g, "<br/>")
			string = string.replace(/\n/g, "<br/>");
			string = string.replace(/\t/g, "&nbsp;");

		} catch (e) {
			console.log(e.message);
		}
		return string;
	},
	/**
	 * 判断字符串是否为空
	 * @param {Object} str
	 * true：不为空
	 * false：为空
	 */
	isNotNull: function(str) {
		if (str != null && str != "" && str != undefined) {
			return true;
		}
		return false;
	},

	/**
	 * 封装消息提示框，默认mui的不支持居中和自定义icon，所以使用h5+
	 * @param {Object} msg
	 * @param {Object} type
	 */
	showToast: function(msg, type,align) {
		if(align==null || align==undefined){
			align = "center"
		}
		
		plus.nativeUI.toast(msg, {
			icon: "/image/" + type + ".png",
			verticalAlign: align
		})
	},

	/**
	 * 保存用户的全局对象
	 * @param {Object} user
	 */
	setUserGlobalInfo: function(user) {
		var userInfoStr = JSON.stringify(user);
		plus.storage.setItem("userInfo", userInfoStr);
	},

	/**
	 * 获取用户的全局对象
	 */
	getUserGlobalInfo: function() {
		var userInfoStr = plus.storage.getItem("userInfo");
		return JSON.parse(userInfoStr);
	},

	/**
	 * 登出后，移除用户全局对象
	 */
	userLogout: function() {
		plus.storage.removeItem("userInfo");
	},

	/**
	 * 保存用户的联系人列表
	 * @param {Object} contactList
	 */
	setContactList: function(contactList) {
		var contactListStr = JSON.stringify(contactList);
		plus.storage.setItem("contactList", contactListStr);
	},

	/**
	 * 获取本地缓存中的联系人列表
	 */
	getContactList: function() {
		var contactListStr = plus.storage.getItem("contactList");

		if (!this.isNotNull(contactListStr)) {
			return [];
		}

		return JSON.parse(contactListStr);
	},

	/**
	 * 根据用户id，从本地的缓存（联系人列表）中获取朋友的信息
	 * @param {Object} friendId
	 */
	getFriendFromContactList: function(friendId) {
		var contactListStr = plus.storage.getItem("contactList");

		// 判断contactListStr是否为空
		if (this.isNotNull(contactListStr)) {
			// 不为空，则把用户信息返回
			var contactList = JSON.parse(contactListStr);
			for (var i = 0; i < contactList.length; i++) {
				var friend = contactList[i];
				if (friend.friendUserId == friendId) {
					return friend;
					break;
				}
			}
		} else {
			// 如果为空，直接返回null
			return null;
		}
	},

	/**
	 * 保存用户的聊天记录
	 * @param {Object} myId
	 * @param {Object} friendId
	 * @param {Object} msg
	 * @param {Object} flag	判断本条消息是我发送的，还是朋友发送的，1:我  2:朋友
	 */
	saveUserChatHistory: function(myId, nick, friendId, msg, type, flag,amrLen) {
		var me = this;
		var chatKey = "chat-" + myId + "-" + friendId;

		// 从本地缓存获取聊天记录是否存在
		var chatHistoryListStr = plus.storage.getItem(chatKey);
		var chatHistoryList;
		if (me.isNotNull(chatHistoryListStr)) {
			// 如果不为空
			chatHistoryList = JSON.parse(chatHistoryListStr);
		} else {
			// 如果为空，赋一个空的list
			chatHistoryList = [];
		}

		// 构建聊天记录对象
		var singleMsg = new me.ChatHistory(myId, nick, friendId, msg, type, flag,amrLen);


		// 向list中追加msg对象
		chatHistoryList.push(singleMsg);
		plus.storage.setItem(chatKey, JSON.stringify(chatHistoryList));
	},

	/**
	 * 获取用户聊天记录
	 * @param {Object} myId
	 * @param {Object} friendId
	 */
	getUserChatHistory: function(myId, friendId) {
		var me = this;
		var chatKey = "chat-" + myId + "-" + friendId;
		var chatHistoryListStr = plus.storage.getItem(chatKey);
		var chatHistoryList;
		if (me.isNotNull(chatHistoryListStr)) {
			// 如果不为空
			chatHistoryList = JSON.parse(chatHistoryListStr);
		} else {
			// 如果为空，赋一个空的list
			chatHistoryList = [];
		}
		return chatHistoryList;
	},

	// 删除我和朋友的聊天记录
	deleteUserChatHistory: function(myId, friendId) {
		var chatKey = "chat-" + myId + "-" + friendId;
		plus.storage.removeItem(chatKey);
	},

	/**
	 * 聊天记录的快照，仅仅保存每次和朋友聊天的最后一条消息
	 * @param {Object} myId
	 * @param {Object} friendId
	 * @param {Object} msg
	 * @param {Object} isRead
	 */
	saveUserChatSnapshot: function(myId, friendId, msg, isRead,type) {
		var me = this;
		var chatKey = "chat-snapshot" + myId;

		// 从本地缓存获取聊天快照的list
		var chatSnapshotListStr = plus.storage.getItem(chatKey);
		var chatSnapshotList;
		if (me.isNotNull(chatSnapshotListStr)) {
			// 如果不为空
			chatSnapshotList = JSON.parse(chatSnapshotListStr);
			// 循环快照list，并且判断每个元素是否包含（匹配）friendId，如果匹配，则删除
			for (var i = 0; i < chatSnapshotList.length; i++) {
				if (chatSnapshotList[i].friendId == friendId) {
					// 删除已经存在的friendId所对应的快照对象
					chatSnapshotList.splice(i, 1);
					break;
				}
			}
		} else {
			// 如果为空，赋一个空的list
			chatSnapshotList = [];
		}

		// 构建聊天快照对象
		var singleMsg = new me.ChatSnapshot(myId, friendId, msg, isRead,type);

		// 向list中追加快照对象
		chatSnapshotList.unshift(singleMsg);

		plus.storage.setItem(chatKey, JSON.stringify(chatSnapshotList));
	},

	/**
	 * 获取用户快照记录列表
	 */
	getUserChatSnapshot: function(myId) {
		var me = this;
		var chatKey = "chat-snapshot" + myId;
		// 从本地缓存获取聊天快照的list
		var chatSnapshotListStr = plus.storage.getItem(chatKey);
		var chatSnapshotList;
		if (me.isNotNull(chatSnapshotListStr)) {
			// 如果不为空
			chatSnapshotList = JSON.parse(chatSnapshotListStr);
		} else {
			// 如果为空，赋一个空的list
			chatSnapshotList = [];
		}

		return chatSnapshotList;
	},

	/**
	 * 删除本地的聊天快照记录
	 * @param {Object} myId
	 * @param {Object} friendId
	 */
	deleteUserChatSnapshot: function(myId, friendId) {
		var me = this;
		var chatKey = "chat-snapshot" + myId;

		// 从本地缓存获取聊天快照的list
		var chatSnapshotListStr = plus.storage.getItem(chatKey);
		var chatSnapshotList;
		if (me.isNotNull(chatSnapshotListStr)) {
			// 如果不为空
			chatSnapshotList = JSON.parse(chatSnapshotListStr);
			// 循环快照list，并且判断每个元素是否包含（匹配）friendId，如果匹配，则删除
			for (var i = 0; i < chatSnapshotList.length; i++) {
				if (chatSnapshotList[i].friendId == friendId) {
					// 删除已经存在的friendId所对应的快照对象
					chatSnapshotList.splice(i, 1);
					break;
				}
			}
		} else {
			// 如果为空，不做处理
			return;
		}

		plus.storage.setItem(chatKey, JSON.stringify(chatSnapshotList));
	},

	/**
	 * 标记未读消息为已读状态
	 * @param {Object} myId
	 * @param {Object} friendId
	 */
	readUserChatSnapshot: function(myId, friendId) {
		var me = this;
		var chatKey = "chat-snapshot" + myId;
		// 从本地缓存获取聊天快照的list
		var chatSnapshotListStr = plus.storage.getItem(chatKey);
		var chatSnapshotList;
		if (me.isNotNull(chatSnapshotListStr)) {
			// 如果不为空
			chatSnapshotList = JSON.parse(chatSnapshotListStr);
			// 循环这个list，判断是否存在好友，比对friendId，
			// 如果有，在list中的原有位置删除该 快照 对象，然后重新放入一个标记已读的快照对象
			for (var i = 0; i < chatSnapshotList.length; i++) {
				var item = chatSnapshotList[i];
				if (item.friendId == friendId) {
					item.isRead = true; // 标记为已读
					chatSnapshotList.splice(i, 1, item); // 替换原有的快照
					break;
				}
			}
			// 替换原有的快照列表
			plus.storage.setItem(chatKey, JSON.stringify(chatSnapshotList));
		} else {
			// 如果为空
			return;
		}
	},

	/**
	 * 和后端的枚举对应
	 */
	CONNECT: 1, // 第一次(或重连)初始化连接
	CHAT: 2, // 聊天消息
	SIGNED: 3, // 消息签收
	KEEPALIVE: 4, // 客户端保持心跳
	PULL_FRIEND: 5, // 重新拉取好友
	//---------------------
	//消息类型
	MSG_TEXT:1,
	MSG_IMAGE:3,
	MSG_SOUND:4,
	MSG_PRIVITE:10001,
	MSG_GROUP:10002,
	

	/**
	 * 和后端的 MixinMsg 聊天模型对象保持一致
	 * @param {Object} senderId
	 * @param {Object} nick  我的昵称
	 * @param {Object} receiverId
	 * @param {Object} msg
	 * @param {Object} type  消息类型
	 * @param {Object} msgId
	 */
	MixinMsg: function(senderId, senderNick, receiverId, msg, type, msgId,amrLen) {
		this.senderId = senderId;
		this.receiverId = receiverId;
		this.msg = msg;
		this.msgId = msgId;
		this.type = type;
		this.senderNick = senderNick;
		if(amrLen!=undefined){
			this.amrLen = amrLen;
		}
	},

	/**
	 * 构建消息 DataContent 模型对象
	 * @param {Object} action
	 * @param {Object} mixinMsg
	 * @param {Object} extand
	 */
	DataContent: function(action, mixinMsg, extand) {
		this.action = action;
		this.mixinMsg = mixinMsg;
		this.extand = extand;
	},

	/**
	 * 单个聊天记录的对象
	 * @param {Object} myId 我的id
	 * @param {Object} friendId 朋友ID
	 * @param {Object} msg 消息
	 */
	ChatHistory: function(senderId, senderNick, receiverId, msg, type, flag,amrLen) {
		this.senderId = senderId;
		this.receiverId = receiverId;
		this.msg = msg;
		this.flag = flag;
		this.type = type;
		this.senderNick = senderNick;
		if(amrLen!=undefined){
			this.amrLen = amrLen;
		}
	},

	/**
	 * 快照对象
	 * @param {Object} myId
	 * @param {Object} friendId
	 * @param {Object} msg
	 * @param {Object} isRead	用于判断消息是否已读还是未读
	 */
	ChatSnapshot: function(myId, friendId, msg, isRead, type) {
		this.myId = myId;
		this.friendId = friendId;
		this.msg = msg;
		this.isRead = isRead;
		this.type = type;

	}

}
