package br.com.tools.kafkamanager.model;

public class MessageKafka {

    private String message;
    private String topic;

    public String getMessage() {
        return message;
    }

    public String getTopic() {
        return topic;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }
}
