import java.nio.charset.StandardCharsets;
import java.util.Base64;
import org.apache.flink.api.common.typeinfo.PrimitiveArrayTypeInfo;
import org.apache.flink.runtime.state.hashmap.HashMapStateBackend;
import org.apache.flink.state.api.OperatorIdentifier;
import org.apache.flink.state.api.SavepointReader;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.CloseableIterator;

public class StateProcessorTest {

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        String savepointPath = StateProcessorTest.class.getResource("/savepoint-27c85e-10ecaba6c582").getPath();
        SavepointReader savepoint = SavepointReader.read(env, savepointPath, new HashMapStateBackend());
        System.out.println(savepoint);
        DataStream<byte[]> listState  = savepoint.readListState(
            OperatorIdentifier.forUid("kafkasourceuid"),
            "SourceReaderState",
            PrimitiveArrayTypeInfo.BYTE_PRIMITIVE_ARRAY_TYPE_INFO);
        CloseableIterator<byte[]> a = listState.executeAndCollect();
        while(a.hasNext()) {
            byte[] aa = a.next();
            System.out.println(aa);
            System.out.println(new String(aa, StandardCharsets.UTF_8));
            System.out.println(new String(aa, StandardCharsets.ISO_8859_1));
            System.out.println(new String(aa, StandardCharsets.US_ASCII));
            System.out.println(new String(aa, StandardCharsets.UTF_16));
            System.out.println(new String(aa, StandardCharsets.UTF_16BE));
            System.out.println(new String(aa, StandardCharsets.UTF_16LE));
            System.out.println(Base64.getEncoder().encodeToString(aa));
            System.out.println(Base64.getMimeEncoder().encodeToString(aa));
        }

        System.out.println("DONE");

        /*
        OUTPUT:
        [B@1f43cab7
                	pageviews           �       
                	pageviews                  
                	pageviews           �       
           	灡来癩敷猀     ր   �
           	灡来癩敷猀     ր   �
           ἀऀ慰敧楶睥s     者   �
        AAAAAAAAAB8ACXBhZ2V2aWV3cwAAAAAAAAAAAAAABYAAAAAAAAAA
        AAAAAAAAAB8ACXBhZ2V2aWV3cwAAAAAAAAAAAAAABYAAAAAAAAAA
        DONE
         */
    }
}
