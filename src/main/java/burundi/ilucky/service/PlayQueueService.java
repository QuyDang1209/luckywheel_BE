package burundi.ilucky.service;

import org.apache.logging.log4j.util.Supplier;
import org.springframework.stereotype.Service;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.LinkedBlockingQueue;

@Service
public class PlayQueueService {

    private final BlockingQueue<Runnable> queue;
    private final Thread worker;

    public PlayQueueService() {
        this.queue = new LinkedBlockingQueue<>(5); // Giới hạn kích thước queue
        worker = new Thread(() -> {
            while (true) {
                try {
                    Runnable task = queue.take(); // Lấy yêu cầu từ queue
                    task.run(); // Xử lý yêu cầu
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
        worker.start();
    }

    public <T> CompletableFuture<T> submitTaskWithResult(Supplier<T> task) {
        CompletableFuture<T> future = new CompletableFuture<>();
        if (queue.remainingCapacity() == 0) {
            // Nếu queue đầy, trả về CompletableFuture với lỗi 429
            future.completeExceptionally(new IllegalStateException("Rate limit exceeded. Try again later."));
            return future;
        }
// Sử dụng take() để chặn khi queue đầy
        try {
            queue.put(() -> { // put() sẽ chặn nếu queue đầy
                try {
                    T result = task.get();
                    future.complete(result);
                } catch (Exception e) {
                    future.completeExceptionally(e);
                }
            });
            System.out.println(queue.size()+"queuue");
        } catch (InterruptedException e) {
            future.completeExceptionally(new IllegalStateException("Thread was interrupted while waiting to add task."));
        }

        return future;
    }
}

