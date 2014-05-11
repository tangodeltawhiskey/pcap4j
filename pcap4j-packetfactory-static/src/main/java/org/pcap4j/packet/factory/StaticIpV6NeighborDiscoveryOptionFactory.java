/*_##########################################################################
  _##
  _##  Copyright (C) 2013-2014  Kaito Yamada
  _##
  _##########################################################################
*/

package org.pcap4j.packet.factory;

import java.util.HashMap;
import java.util.Map;
import org.pcap4j.packet.IcmpV6CommonPacket.IpV6NeighborDiscoveryOption;
import org.pcap4j.packet.IllegalIpV6NeighborDiscoveryOption;
import org.pcap4j.packet.IllegalRawDataException;
import org.pcap4j.packet.IpV6NeighborDiscoveryMtuOption;
import org.pcap4j.packet.IpV6NeighborDiscoveryPrefixInformationOption;
import org.pcap4j.packet.IpV6NeighborDiscoveryRedirectedHeaderOption;
import org.pcap4j.packet.IpV6NeighborDiscoverySourceLinkLayerAddressOption;
import org.pcap4j.packet.IpV6NeighborDiscoveryTargetLinkLayerAddressOption;
import org.pcap4j.packet.UnknownIpV6NeighborDiscoveryOption;
import org.pcap4j.packet.namednumber.IpV6NeighborDiscoveryOptionType;

/**
 * @author Kaito Yamada
 * @since pcap4j 0.9.16
 */
public final class StaticIpV6NeighborDiscoveryOptionFactory
implements PacketFactory<IpV6NeighborDiscoveryOption, IpV6NeighborDiscoveryOptionType> {

  private static final StaticIpV6NeighborDiscoveryOptionFactory INSTANCE
    = new StaticIpV6NeighborDiscoveryOptionFactory();
  private final Map<IpV6NeighborDiscoveryOptionType, Instantiater> instantiaters
    = new HashMap<IpV6NeighborDiscoveryOptionType, Instantiater>();

  private StaticIpV6NeighborDiscoveryOptionFactory() {
    instantiaters.put(
      IpV6NeighborDiscoveryOptionType.SOURCE_LINK_LAYER_ADDRESS, new Instantiater() {
        @Override
        public IpV6NeighborDiscoveryOption newInstance(
          byte[] rawData
        ) throws IllegalRawDataException {
          return IpV6NeighborDiscoverySourceLinkLayerAddressOption.newInstance(rawData);
        }
      }
    );
    instantiaters.put(
      IpV6NeighborDiscoveryOptionType.TARGET_LINK_LAYER_ADDRESS, new Instantiater() {
        @Override
        public IpV6NeighborDiscoveryOption newInstance(
          byte[] rawData
        ) throws IllegalRawDataException {
          return IpV6NeighborDiscoveryTargetLinkLayerAddressOption.newInstance(rawData);
        }
      }
    );
    instantiaters.put(
      IpV6NeighborDiscoveryOptionType.PREFIX_INFORMATION, new Instantiater() {
        @Override
        public IpV6NeighborDiscoveryOption newInstance(
          byte[] rawData
        ) throws IllegalRawDataException {
          return IpV6NeighborDiscoveryPrefixInformationOption.newInstance(rawData);
        }
      }
    );
    instantiaters.put(
      IpV6NeighborDiscoveryOptionType.REDIRECTED_HEADER, new Instantiater() {
        @Override
        public IpV6NeighborDiscoveryOption newInstance(
          byte[] rawData
        ) throws IllegalRawDataException {
          return IpV6NeighborDiscoveryRedirectedHeaderOption.newInstance(rawData);
        }
      }
    );
    instantiaters.put(
      IpV6NeighborDiscoveryOptionType.MTU, new Instantiater() {
        @Override
        public IpV6NeighborDiscoveryOption newInstance(
          byte[] rawData
        ) throws IllegalRawDataException {
          return IpV6NeighborDiscoveryMtuOption.newInstance(rawData);
        }
      }
    );
  };

  /**
   *
   * @return the singleton instance of StaticIpV6NeighborDiscoveryOptionFactory.
   */
  public static StaticIpV6NeighborDiscoveryOptionFactory getInstance() {
    return INSTANCE;
  }

  public IpV6NeighborDiscoveryOption newInstance(
    byte[] rawData, IpV6NeighborDiscoveryOptionType number
  ) {
    if (rawData == null || number == null) {
      StringBuilder sb = new StringBuilder(40);
      sb.append("rawData: ")
        .append(rawData)
        .append(" number: ")
        .append(number);
      throw new NullPointerException(sb.toString());
    }

    try {
      Instantiater instantiater = instantiaters.get(number);
      if (instantiater != null) {
        return instantiater.newInstance(rawData);
      }
    } catch (IllegalRawDataException e) {
      return IllegalIpV6NeighborDiscoveryOption.newInstance(rawData);
    }

    return newInstance(rawData);
  }

  public IpV6NeighborDiscoveryOption newInstance(byte[] rawData) {
    try {
      return UnknownIpV6NeighborDiscoveryOption.newInstance(rawData);
    } catch (IllegalRawDataException e) {
      return IllegalIpV6NeighborDiscoveryOption.newInstance(rawData);
    }
  }

  private static abstract class Instantiater {

    public abstract IpV6NeighborDiscoveryOption newInstance(
      byte [] rawData
    ) throws IllegalRawDataException;

  }

}
