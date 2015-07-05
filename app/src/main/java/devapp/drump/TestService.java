package devapp.drump;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by arseniy on 05/07/15.
 */
public class TestService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
