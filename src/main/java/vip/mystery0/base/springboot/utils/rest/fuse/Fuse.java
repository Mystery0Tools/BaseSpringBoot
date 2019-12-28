package vip.mystery0.base.springboot.utils.rest.fuse;

/**
 * 请求熔断器
 *
 * @author mystery0
 * Create at 2019/12/24
 */
public class Fuse {
    public interface Listener {
        void onFuse();

        default String getRequestURIByUrl(String url) {
            return url;
        }
    }
}
