/**
 * 
 */
package com.amazonaws.samples;

/**
 * @author v.lai
 *
 */
import java.util.Arrays;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.ScalarAttributeType;

public class MoviesCreateTable {

    public static void main(String[] args) throws Exception {

        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
            .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("http://localhost:8000", "us-west-2"))
            .build();
       //エンドポイントを設定した後はDYNAMODBにテーブルを作成ことをしめする。

        DynamoDB dynamoDB = new DynamoDB(client);

        String tableName = "Movies";

        try {
            System.out.println("Attempting to create table; please wait...");
            //createTable 呼び出しでは、テーブル名、プライマリキー属性、そのデータ型を指定します
            Table table = dynamoDB.createTable(tableName,
                Arrays.asList(new KeySchemaElement("year", KeyType.HASH), // Partition パーティションキーを追加する
                                                                          // key
                    new KeySchemaElement("title", KeyType.RANGE)), // Sort key    ソートキーを追加する
                Arrays.asList(new AttributeDefinition("year", ScalarAttributeType.N), // Yearの価値タイプを追加
                    new AttributeDefinition("title", ScalarAttributeType.S)), // Titleの価値タイプを追加
                new ProvisionedThroughput(10L, 10L));  //テーブルがサポートできる読み込みおよび書き込みアクティビティの量
            table.waitForActive();
            System.out.println("Success.  Table status: " + table.getDescription().getTableStatus());
            System.out.println("All Status: " + table.getDescription());
            // getDescription
            //______________
            // {AttributeDefinitions: [{AttributeName: year,AttributeType: N}, 
            // {AttributeName: title,AttributeType: S}],TableName: Movies,KeySchema: [
            // 			{AttributeName: year,KeyType: HASH}, {AttributeName: title,KeyType: RANGE}
            // ],
            // TableStatus: ACTIVE,CreationDateTime: Fri Oct 19 10:21:56 JST 2018,
            // ProvisionedThroughput: {
            // 		LastIncreaseDateTime: Thu Jan 01 09:00:00 JST 1970,
            //		LastDecreaseDateTime: Thu Jan 01 09:00:00 JST 1970,
            //		NumberOfDecreasesToday: 0,ReadCapacityUnits: 10,WriteCapacityUnits: 10
            //		},
            // TableSizeBytes: 0,ItemCount: 0,
            // TableArn: arn:aws:dynamodb:ddblocal:000000000000:table/Movies,}

        }
        catch (Exception e) {
            System.err.println("Unable to create table: ");
            System.err.println(e.getMessage());
        }

    }
}