import SockJS from 'sockjs-client'
import { getToken } from '@/utils/auth'
import store from '../store'
import Stomp from 'stompjs'

let stompClient = null
let socket = null
let connected = false

const subscribes = {
  createConsumption: {
    queue: '/user/queue/consumption',
    callback(msg) {
      if (msg.command === 'MESSAGE') {
        subscribes.createConsumption.realCallback(msg)
      }
    },
    realCallback(msg) {},
    send(message) {
      const headers = {
        authorization: getToken()
      }
      if (stompClient == null || socket == null) {
        Connector.connect(() => {
          stompClient.send('/app/consumption', headers, message)
        })
      } else {
        stompClient.send('/app/consumption', headers, message)
      }
    },
    subId: null
  },
  createIncome: {
    queue: '/user/queue/income',
    callback(msg) {
      if (msg.command === 'MESSAGE') {
        subscribes.createIncome.realCallback(msg)
      }
    },
    realCallback(msg) {},
    send(message) {
      const headers = {
        authorization: getToken()
      }
      if (stompClient == null || socket == null) {
        Connector.connect(() => {
          stompClient.send('/app/income', headers, message)
        })
      } else {
        stompClient.send('/app/income', headers, message)
      }
    },
    subId: null
  },
  error: {
    queue: '/user/queue/error',
    callback(msg) {
      if (msg.command === 'MESSAGE') {
        const { body } = msg
        console.log(body)
      }
    },
    send(message) {},
    subId: null
  },
  selfSettings: {
    queue: '/user/topic/selfConfig',
    callback(msg) {
      if (msg.command === 'MESSAGE') {
        const { body } = msg
        console.log(body)
        const info = JSON.parse(localStorage.getItem('info'))
        if (info) {
          info.selfConfig = JSON.parse(body)
          localStorage.setItem('info', JSON.stringify(info))
        }
        store.dispatch('user/selfSettings', JSON.parse(body)).catch(error => {
          console.log(error)
        })
      }
    },
    send(message) {},
    subId: null
  },
  businessSettings: {
    queue: '/topic/businessSetting',
    callback(msg) {
      if (msg.command === 'MESSAGE') {
        const { body } = msg
        console.log(body)
        const info = JSON.parse(localStorage.getItem('info'))
        if (info) {
          info.businessSetting = JSON.parse(body)
          localStorage.setItem('info', JSON.stringify(info))
        }
        store.dispatch('user/businessSettings', JSON.parse(body)).catch(error => {
          console.log(error)
        })
      }
    },
    send(message) {},
    subId: null
  }
}

export const Connector = {
  connect(callback) {
    // 建立连接对象
    socket = new SockJS('http://apollo.free.idcfengye.com/socket')
    // socket = new SockJS('http://:8101/pushServer')

    // 获取STOMP子协议的客户端对象
    stompClient = Stomp.over(socket)
    // 向服务器发起websocket连接
    stompClient.connect({
      authorization: getToken()
    }, () => {
      console.log('连接成功')
      connected = true
      for (const key in subscribes) {
        const subscribe = subscribes[key]
        const headers = {
          authorization: getToken()
        }
        stompClient.subscribe(subscribe.queue, subscribe.callback, headers)
        subscribe.subId = headers.id
        if (callback) {
          callback()
        }
      }
    }, err => {
      // 连接发生错误时的处理函数
      console.log('连接被远程终止，即将重试')
      console.log(err)
      setTimeout(() => {
        Connector.connect()
      }, 2000)
    })
  },
  disconnect() {
    if (stompClient) {
      const headers = {
        authorization: getToken()
      }
      console.log('取消订阅')
      for (const key in subscribes) {
        const subscribe = subscribes[key]
        stompClient.unsubscribe(subscribe.subId, headers)
      }
      console.log('断开连接')
      stompClient.disconnect(() => {
        socket.close()
        socket = null
        stompClient = null
      }, headers)
      connected = false
    }
  },
  subscribes: subscribes,
  connected: connected
}
