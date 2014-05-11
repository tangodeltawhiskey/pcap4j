/*_##########################################################################
  _##
  _##  Copyright (C) 2014  Kaito Yamada
  _##
  _##########################################################################
*/

package org.pcap4j.packet;

import java.util.ArrayList;
import java.util.List;
import org.pcap4j.packet.namednumber.Ssh2MessageNumber;
import org.pcap4j.util.ByteArrays;

/**
 * @author Kaito Yamada
 * @since pcap4j 1.0.1
 */
public final class Ssh2UnimplementedPacket extends AbstractPacket {

  /**
   *
   */
  private static final long serialVersionUID = -8439655903366307992L;

  private final Ssh2UnimplementedHeader header;

  /**
   *
   * @param rawData
   * @return a new Ssh2UnimplementedPacket object.
   * @throws IllegalRawDataException
   */
  public static Ssh2UnimplementedPacket newPacket(
    byte[] rawData
  ) throws IllegalRawDataException {
    return new Ssh2UnimplementedPacket(rawData);
  }

  private Ssh2UnimplementedPacket(byte[] rawData) throws IllegalRawDataException {
    if (rawData == null) {
      throw new NullPointerException();
    }
    this.header = new Ssh2UnimplementedHeader(rawData);
  }

  private Ssh2UnimplementedPacket(Builder builder) {
    if (builder == null) {
      StringBuilder sb = new StringBuilder();
      sb.append("builder: ").append(builder);
      throw new NullPointerException(sb.toString());
    }

    this.header = new Ssh2UnimplementedHeader(builder);
  }

  @Override
  public Ssh2UnimplementedHeader getHeader() {
    return header;
  }

  @Override
  public Builder getBuilder() {
    return new Builder(this);
  }

  /**
   *
   * @author Kaito Yamada
   * @since pcap4j 1.0.1
   */
  public static final class Builder extends AbstractBuilder {

    private int sequenceNumber;

    /**
     *
     */
    public Builder() {}

    private Builder(Ssh2UnimplementedPacket packet) {
      this.sequenceNumber = packet.header.sequenceNumber;
    }

    /**
     *
     * @param sequenceNumber
     * @return this Builder object for method chaining.
     */
    public Builder sequenceNumber(int sequenceNumber) {
      this.sequenceNumber = sequenceNumber;
      return this;
    }

    @Override
    public Ssh2UnimplementedPacket build() {
      return new Ssh2UnimplementedPacket(this);
    }

  }

  /**
   *
   * @author Kaito Yamada
   * @version pcap4j 1.0.1
   */
  public static final class Ssh2UnimplementedHeader extends AbstractHeader {

    /*
     * http://tools.ietf.org/html/rfc4253
     *
     * byte      SSH_MSG_UNIMPLEMENTED
     * uint32    packet sequence number of rejected message
     */

    /**
     *
     */
    private static final long serialVersionUID = 1942311282988657234L;

    private final Ssh2MessageNumber messageNumber = Ssh2MessageNumber.SSH_MSG_UNIMPLEMENTED;
    private final int sequenceNumber;

    private Ssh2UnimplementedHeader(byte[] rawData) throws IllegalRawDataException {
      if (rawData.length < 5) {
        StringBuilder sb = new StringBuilder(80);
        sb.append("The data is too short to build an SSH2 Unimplemented header. data: ")
          .append(new String(rawData));
        throw new IllegalRawDataException(sb.toString());
      }

      if (!Ssh2MessageNumber.getInstance(rawData[0]).equals(Ssh2MessageNumber.SSH_MSG_UNIMPLEMENTED)) {
        StringBuilder sb = new StringBuilder(120);
        sb.append("The data is not an SSH2 Unimplemented message. data: ")
          .append(new String(rawData));
        throw new IllegalRawDataException(sb.toString());
      }

      this.sequenceNumber = ByteArrays.getInt(rawData, 1);
    }

    private Ssh2UnimplementedHeader(Builder builder) {
      this.sequenceNumber = builder.sequenceNumber;
    }

    /**
     *
     * @return messageNumber
     */
    public Ssh2MessageNumber getMessageNumber() {
      return messageNumber;
    }

    /**
     *
     * @return sequenceNumber
     */
    public int getSequenceNumber() {
      return sequenceNumber;
    }

    /**
     *
     * @return sequenceNumber
     */
    public long getSequenceNumberAsLong() {
      return sequenceNumber & 0xFFFFFFFFL;
    }

    @Override
    protected List<byte[]> getRawFields() {
      List<byte[]> rawFields = new ArrayList<byte[]>();
      rawFields.add(new byte[] {messageNumber.value()});
      rawFields.add(ByteArrays.toByteArray(sequenceNumber));
      return rawFields;
    }

    @Override
    public int length() { return 5; }

    @Override
    protected String buildString() {
      StringBuilder sb = new StringBuilder();
      String ls = System.getProperty("line.separator");

      sb.append("[SSH2 Unimplemented Header (")
        .append(length())
        .append(" bytes)]")
        .append(ls);
      sb.append("  Message Number: ")
        .append(messageNumber)
        .append(ls);
      sb.append("  packet sequence number: ")
        .append(getSequenceNumberAsLong())
        .append(ls);

      return sb.toString();
    }

  }

}
