package foo.bar.example.asafadapters2.feature.remote;

import java.util.ArrayList;
import java.util.List;

import co.early.asaf.core.Affirm;
import co.early.asaf.core.WorkMode;
import co.early.asaf.core.callbacks.FailureCallbackWithPayload;
import co.early.asaf.core.callbacks.SuccessCallback;
import co.early.asaf.core.logging.Logger;
import co.early.asaf.core.observer.ObservableImp;
import co.early.asaf.core.time.SystemTimeWrapper;
import co.early.asaf.retrofit.CallProcessor;
import foo.bar.example.asafadapters2.App;
import foo.bar.example.asafadapters2.R;
import foo.bar.example.asafadapters2.api.todoitems.TodoItemPojo;
import foo.bar.example.asafadapters2.api.todoitems.TodoItemService;
import foo.bar.example.asafadapters2.feature.todoitems.TodoItem;
import foo.bar.example.asafadapters2.feature.todoitems.TodoListModel;
import foo.bar.example.asafadapters2.message.UserMessage;

/**
 * gets a list of todoitems from the network and adds them to the database
 */
public class RemoteWorker extends ObservableImp{

    public static final String LOG_TAG = RemoteWorker.class.getSimpleName();

    //notice how we use the TodoListModel, we don't go directly to the db layer
    private final TodoListModel todoListModel;
    private final TodoItemService service;
    private final CallProcessor<UserMessage> callProcessor;
    private final SystemTimeWrapper systemTimeWrapper;
    private final WorkMode workMode;
    private final Logger logger;

    private int connections;
    private static final String WEB = App.instance().getString(R.string.todo_web);

    public RemoteWorker(TodoListModel todoListModel, TodoItemService service, CallProcessor<UserMessage> callProcessor,
                        SystemTimeWrapper systemTimeWrapper, Logger logger, WorkMode workMode) {
        super(workMode);
        this.todoListModel = Affirm.notNull(todoListModel);
        this.service = Affirm.notNull(service);
        this.callProcessor = Affirm.notNull(callProcessor);
        this.systemTimeWrapper = Affirm.notNull(systemTimeWrapper);
        this.logger = Affirm.notNull(logger);
        this.workMode = Affirm.notNull(workMode);
    }

    public void fetchTodoItems(final SuccessCallback successCallback, final FailureCallbackWithPayload<UserMessage> failureCallbackWithPayload){

        logger.i(LOG_TAG, "fetchTodoItems()");

        Affirm.notNull(successCallback);
        Affirm.notNull(failureCallbackWithPayload);

        if (connections>8){
            failureCallbackWithPayload.fail(UserMessage.ERROR_BUSY);
            return;
        }


        connections++;
        notifyObservers();

        callProcessor.processCall(service.getTodoItems("3s"), workMode,
                successResponse -> handleNetworkSuccess(successCallback, successResponse),
                failureMessage -> handleNetworkFailure(failureCallbackWithPayload, failureMessage));

    }

    private void handleNetworkSuccess(SuccessCallback successCallBack, List<TodoItemPojo> todoItemPojos){
        addTodoItemsToDatabase(todoItemPojos);
        successCallBack.success();
        complete();
    }

    private void handleNetworkFailure(FailureCallbackWithPayload<UserMessage> failureCallbackWithPayload, UserMessage failureMessage){
        failureCallbackWithPayload.fail(failureMessage);
        complete();
    }

    private void addTodoItemsToDatabase(List<TodoItemPojo> todoItemPojos){

        List<TodoItem> todoItems = new ArrayList<>(todoItemPojos.size());

        for (TodoItemPojo todoItemPojo : todoItemPojos){
            todoItems.add(new TodoItem(systemTimeWrapper.currentTimeMillis(), WEB + connections + " " + todoItemPojo.label));
        }

        todoListModel.addMany(todoItems);
    }

    public boolean isBusy() {
        return connections>0;
    }

    public int getConnections() {
        return connections;
    }

    private void complete(){

        logger.i(LOG_TAG, "complete()");

        connections--;
        notifyObservers();
    }

}