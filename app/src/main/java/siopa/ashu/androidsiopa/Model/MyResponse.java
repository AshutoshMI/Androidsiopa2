package siopa.ashu.androidsiopa.Model;

import java.util.List;

/**
 * Created by DELL on 5/28/2018.
 */

public class MyResponse {
    public long multicast_id;
    public int success;
    public int failure;
    public int canonical_ids;
    public List<Result>results;
}
