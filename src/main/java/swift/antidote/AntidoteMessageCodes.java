package swift.antidote;

/**
 * For details, see:
 * https://github.com/SyncFree/riak_pb/blob/antidote/src/riak_pb_messages.csv
 */
public class AntidoteMessageCodes {
    public static final byte MSG_IncrementReq = 94;
    public static final byte MSG_DecrementReq = 95;
    public static final byte MSG_OperationResp = 96;
    public static final byte MSG_GetCounterReq = 97;
    public static final byte MSG_GetCounterResp = 98;
    public static final byte MSG_SetUpdateReq = 99;
    public static final byte MSG_GetSetReq = 100;
    public static final byte MSG_GetSetResp = 101;
    public static final byte MSG_AtomicUpdateTxnReq = 102;
    public static final byte MSG_AtomicUpdateTxnOps = 103;
    public static final byte MSG_AtomicUpdateTxnResp = 104;
    public static final byte MSG_SnapshotReadTxnReq = 105;
    public static final byte MSG_SnapshotReadTxnOp = 106;
    public static final byte MSG_SnapshotReadTxnRespValue = 107;
    public static final byte MSG_SnapshotReadTxnResp	= 108;
}
